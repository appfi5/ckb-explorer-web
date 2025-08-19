package com.ckb.explorer.domain.resp;

import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;
import lombok.NoArgsConstructor;

/**
 * BlockResponse 用于将 Block 实体序列化为 JSON 响应对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlockResponse extends BaseResponse<Long> {

  private String blockHash;
  private List<String> uncleBlockHashes;
  private String minerHash;
  private String transactionsRoot;
  private String rewardStatus;
  private String receivedTxFeeStatus;
  private String minerMessage;

  private Long number; // from object.number.to_s
  private Long startNumber;
  private Integer length;
  private String version;
  private Integer proposalsCount;
  private Integer unclesCount;
  private Long timestamp;
  private Long reward;
  private Long cellConsumed;
  private Long totalTransactionFee;
  private Integer transactionsCount; // from object.ckb_transactions_count.to_s
  private Long totalCellCapacity;
  private Long receivedTxFee;
  private String epoch;
  private Long blockIndexInEpoch;
  private String nonce;
  private String difficulty;
  private Long minerReward; // (object.received_tx_fee + object.reward).to_s
  private Integer size; // 逻辑由业务层处理（如异步更新）
  private Long largestBlockInEpoch; // from object.epoch_statistic&.largest_block_size
  private Long largestBlock; // from EpochStatistic.largest_block_size
  private Long cycles;
  private Long maxCyclesInEpoch; // from object.epoch_statistic&.max_block_cycles
  private Long maxCycles; // from EpochStatistic.max_block_cycles
}
