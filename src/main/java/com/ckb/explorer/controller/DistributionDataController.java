package com.ckb.explorer.controller;

import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.resp.DistributionDataResponse;
import com.ckb.explorer.facade.IDistributionDataCacheFacade;
import com.ckb.explorer.util.I18n;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DistributionDataController 提供分布数据相关的API接口 对应Ruby代码中的Api::V1::DistributionDataController
 */
@RestController
@RequestMapping("/api/v1/distribution_data")
public class DistributionDataController {

  public static final Set<String> VALID_INDICATORS = Set.of(
      "address_balance_distribution",
      "block_time_distribution",
      "epoch_time_distribution",
//      "epoch_length_distribution", 页面不展示
      "average_block_time"
//      "nodes_distribution",
//      "miner_address_distribution" 等以后矿工表
  );

  @Resource
  private I18n i18n;

  @Resource
  private IDistributionDataCacheFacade distributionDataCacheFacade;


  /**
   * 获取分布数据
   *
   * @param indicator 指标名称
   * @return 分布数据响应
   */
  @GetMapping("/{indicator}")
  @Operation(summary = "获取分布数据")
  public ResponseEntity<ResponseInfo<DistributionDataResponse>> show(@PathVariable("indicator") String indicator) {
    // 验证查询参数
    validateQueryParams(indicator.trim());
    DistributionDataResponse data;
    // 如果指标是平均区块时间，使用特定的方法
    if ("average_block_time".equals(indicator.trim())) {
      data = distributionDataCacheFacade.getAverageBlockTime();
    } else{
      data = distributionDataCacheFacade.getDistributionDataByIndicator(indicator.trim());
    }

    // 构建响应
    ResponseInfo response = ResponseInfo.SUCCESS(data);

    // 对应ruby expires_in 1.hour, public: true, stale_while_revalidate: 10.minutes, stale_if_error: 1.hour
    CacheControl cacheControl = CacheControl.maxAge(60, TimeUnit.MINUTES)
        .cachePublic() // 允许公共缓存（CDN、代理服务器等）
        .staleWhileRevalidate(10, TimeUnit.MINUTES)
        .staleIfError(60, TimeUnit.MINUTES);

    return ResponseEntity.ok()
        .cacheControl(cacheControl)
        .body(response);
  }

  private static final Pattern MINER_ADDRESS_PATTERN = Pattern.compile(
      "^miner_address_distribution(\\d+)$");

  /**
   * 验证查询参数
   *
   * @param indicator 指标名称
   */
  private void validateQueryParams(String indicator) {
    if (indicator == null || indicator.isEmpty()) {
      throw new IllegalArgumentException("Indicator name cannot be empty");
    }
    String[] parts = indicator.split("-");
    for (String part : parts) {
      if (!VALID_INDICATORS.contains(part) && !MINER_ADDRESS_PATTERN.matcher(part).matches()) {
        throw new ServerException(I18nKey.INDICATOR_NAME_INVALID_CODE,
            i18n.getMessage(I18nKey.INDICATOR_NAME_INVALID_MESSAGE));
      }
    }
  }
}