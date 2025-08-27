package com.ckb.explorer.domain.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nervos.ckb.type.BlockchainInfo;

/**
 * 统计响应类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatisticResponse {

  private String type = "statistic";
  /**
   * 最新区块高度
   */
  private Long tipBlockNumber;

  /**
   * 平均区块时间
   */
  private String averageBlockTime;

  /**
   * 当前纪元难度
   */
  private String currentEpochDifficulty;

  /**
   * 哈希率
   */
  private String hashRate;

  /**
   * 矿工排名
   */
  private Object minerRanking;

  /**
   * 区块链信息
   */
  private BlockchainInfoResponse blockchainInfo;

  /**
   * 缓存刷新信息
   */
  private Object flushCacheInfo;

  /**
   * 地址余额排名
   */
  private Object addressBalanceRanking;

  /**
   * 维护信息
   */
  private Object maintenanceInfo;

  /**
   * 创建时间戳（UNIX格式）
   */
  private Long createdAtUnixtimestamp;
}
