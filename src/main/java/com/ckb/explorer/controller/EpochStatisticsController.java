package com.ckb.explorer.controller;

import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.resp.EpochStatisticsResponse;
import com.ckb.explorer.facade.IEpochStatisticsCacheFacade;
import com.ckb.explorer.util.I18n;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;

/**
 * EpochStatisticsController 处理纪元统计数据相关的HTTP请求
 */
@RestController
@RequestMapping("/api/v1/epoch_statistics")
@Validated
@Slf4j
public class EpochStatisticsController {

  public static final Set<String> VALID_INDICATORS = Set.of(
      "difficulty",
      "uncle_rate",
      "hash_rate",
      "epoch_time",
      "epoch_length"
  );

  @Resource
  private IEpochStatisticsCacheFacade epochStatisticsCacheFacade;

  @Resource
  private I18n i18n;

  /**
   * 获取纪元统计数据
   *
   * @param indicator 指标名称
   * @param limit     限制数量（可选）
   * @return 纪元统计数据JSON响应
   */
  @GetMapping("/{indicator}")
  public ResponseEntity<ResponseInfo<List<EpochStatisticsResponse>>> show(
      @PathVariable("indicator") String indicator,
      @RequestParam(value = "limit", required = false) @Positive Integer limit) {

    // 验证查询参数
    validateQueryParams(indicator.trim(), limit);

    // 调用缓存门面获取数据
    List<EpochStatisticsResponse> data = epochStatisticsCacheFacade.getEpochStatistics(limit, indicator.trim());

    // 构建响应
    ResponseInfo response = ResponseInfo.SUCCESS(data);

    // 对应ruby expires_in 30.minutes, public: true, stale_while_revalidate: 10.minutes, stale_if_error: 10.minutes
    CacheControl cacheControl = CacheControl.maxAge(30, TimeUnit.MINUTES) // 缓存30分钟
        .cachePublic() // 允许公共缓存（CDN、代理服务器等）
        .staleWhileRevalidate(10, TimeUnit.MINUTES) //  stale-while-revalidate: 10分钟
        .staleIfError(10, TimeUnit.MINUTES);   // stale-if-error: 10分钟

    return ResponseEntity.ok()
        .cacheControl(cacheControl)
        .body(response);

  }

  /**
   * 验证查询参数
   *
   * @param indicator 指标名称
   * @param limit     限制数量
   * @throws IllegalArgumentException 当参数无效时抛出异常
   */
  private void validateQueryParams(String indicator, Integer limit) {
    // 验证指标名称不能为空
    if (indicator == null || indicator.isEmpty()) {
      throw new IllegalArgumentException("Indicator name cannot be empty");
    }

    String[] parts = indicator.split("-");
    for (String part : parts) {
      if (!VALID_INDICATORS.contains(part)) {
        throw new ServerException(I18nKey.INDICATOR_NAME_INVALID_CODE,
            i18n.getMessage(I18nKey.INDICATOR_NAME_INVALID_MESSAGE));
      }
    }
  }
}