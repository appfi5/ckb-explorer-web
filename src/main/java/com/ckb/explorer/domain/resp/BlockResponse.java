package com.ckb.explorer.domain.resp;

import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.math.BigInteger;
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

  private String type = "block";
  private String blockHash;
  private List<String> uncleBlockHashes;
  private String minerHash;
  private String transactionsRoot;
  private String rewardStatus;// TODO 状态待定，需定时任务刷
  private String receivedTxFeeStatus;
  private String minerMessage;

  private Long number;
  private Long startNumber;
  private Integer length;
  private String version;
  private Integer proposalsCount;
  private Integer unclesCount;
  private Long timestamp;
  private Long reward;// TODO 待定先不要
  private Long cellConsumed;
  private Long totalTransactionFee;
  private Integer transactionsCount; // from object.ckb_transactions_count.to_s
  @JsonSerialize(using = ToStringSerializer.class)
  private Long totalCellCapacity;
  private Long receivedTxFee;// TODO 待定先不要
  private Long epoch; // 对应表里的epochNumber
  private Long blockIndexInEpoch;
  private String nonce;
  private BigInteger difficulty;
  private Long minerReward; // 原(object.received_tx_fee + object.reward).to_s 现11个块之后的reward值
  private Integer size; // 原异步更新UpdateBlockSizeWorker.perform_async 现blockSize
  private Long largestBlockInEpoch; // TODO from object.epoch_statistic&.largest_block_size
  private Long largestBlock; // TODO from EpochStatistic.largest_block_size
  private Long cycles;
  private Long maxCyclesInEpoch; // TODO from object.epoch_statistic&.max_block_cycles
  private Long maxCycles; // TODO from EpochStatistic.max_block_cycles
}
