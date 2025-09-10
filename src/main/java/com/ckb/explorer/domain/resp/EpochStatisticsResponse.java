package com.ckb.explorer.domain.resp;

import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EpochStatisticsResponse extends BaseResponse<Long> {
  private String type = "epoch_statistic";

  /**
   * Epoch编号
   */
  private Long epochNumber;

  /**
   * 难度
   */
  private String difficulty;

  /**
   * Uncle率
   */
  private BigDecimal uncleRate;

  /**
   * 哈希率
   */
  private String hashRate;

  /**
   * Epoch时长
   */
  private Long epochTime;

  /**
   * Epoch长度（区块数）
   */
  private Integer epochLength;

  private LargestBlockResponse largestBlock;

  private LargestTxResponse largestTx;

}
