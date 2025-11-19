package com.ckb.explorer.controller;

import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.resp.DailyStatisticResponse;
import com.ckb.explorer.facade.DailyStatisticsCacheFacade;
import com.ckb.explorer.util.I18n;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DailyStatisticsController 提供每日统计数据相关的API接口
 */
@RestController
@RequestMapping("/api/v1/daily_statistics")
public class DailyStatisticsController {

  public static final Set<String> VALID_INDICATORS = Set.of(
      "transactions_count",
      "addresses_count",
      "total_dao_deposit",
      "live_cells_count",
      "dead_cells_count",
      "avg_hash_rate",
      "avg_difficulty",
      "uncle_rate",
      "total_depositors_count",
      "total_tx_fee",
//      "occupied_capacity",页面不展示
      "daily_dao_deposit",
      "daily_dao_depositors_count",
      "circulation_ratio",
      "circulating_supply",
      "burnt",
      "locked_capacity",
      "treasury_amount",
      "mining_reward",
      "deposit_compensation",
      "liquidity",
      "created_at_unixtimestamp",
      "ckb_hodl_wave",
      "holder_count",
      "knowledge_size",
      "activity_address_contract_distribution"
  );

  @Resource
  private I18n i18n;

  @Resource
  private DailyStatisticsCacheFacade dailyStatisticsCacheFacade;

  /**
   * 获取指定指标的每日统计数据
   *
   * @param indicator 指标名称
   * @return 包含统计数据的响应
   */
  @GetMapping("/{indicator}")
  public ResponseEntity<ResponseInfo<List<DailyStatisticResponse>>> show(
      @PathVariable("indicator") String indicator) {
    // 验证查询参数
    validateQueryParams(indicator.trim());

    // 从缓存门面获取数据
    List<DailyStatisticResponse> dailyStatistics = dailyStatisticsCacheFacade.getDailyStatisticsByIndicator(
        indicator.trim());

    CacheControl cacheControl = CacheControl.maxAge(60, TimeUnit.MINUTES)
        .cachePublic() // 允许公共缓存（CDN、代理服务器等）
        .staleWhileRevalidate(10, TimeUnit.MINUTES)
        .staleIfError(60, TimeUnit.MINUTES);

    // 返回成功响应
    return ResponseEntity.ok()
        .cacheControl(cacheControl)
        .body(ResponseInfo.SUCCESS(dailyStatistics));
  }

  /**
   * 验证查询参数
   *
   * @param indicator 指标名称
   */
  private void validateQueryParams(String indicator) {
    if (indicator == null || indicator.isEmpty()) {
      throw new IllegalArgumentException("Indicator name cannot be empty");
    }
    // !(query_key.split("-") - ::DailyStatistic::VALID_INDICATORS).empty?
    String[] parts = indicator.split("-");
    for (String part : parts) {
      if (!VALID_INDICATORS.contains(part)) {
        throw new ServerException(I18nKey.INDICATOR_NAME_INVALID_CODE,
            i18n.getMessage(I18nKey.INDICATOR_NAME_INVALID_MESSAGE));
      }
    }
  }
}