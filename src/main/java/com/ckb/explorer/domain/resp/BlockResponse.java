package com.ckb.explorer.domain.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.List;

/**
 * BlockResponse 用于将 Block 实体序列化为 JSON 响应对象
 */
@Data
// @JsonInclude(JsonInclude.Include.NON_NULL) // 忽略 null 字段
public class BlockResponse {

  private String block_hash;
  private List<String> uncle_block_hashes;
  private String miner_hash;
  private String transactions_root;
  private String reward_status;
  private String received_tx_fee_status;
  private String miner_message;

  private String number; // from object.number.to_s
  private String start_number;
  private String length;
  private String version;
  private String proposals_count;
  private String uncles_count;
  private String timestamp;
  private String reward;
  private String cell_consumed;
  private String total_transaction_fee;
  private String transactions_count; // from object.ckb_transactions_count.to_s
  private String total_cell_capacity;
  private String received_tx_fee;
  private String epoch;
  private String block_index_in_epoch;
  private String nonce;
  private String difficulty;
  private String miner_reward; // (object.received_tx_fee + object.reward).to_s
  private String size; // 逻辑由业务层处理（如异步更新）
  private String largest_block_in_epoch; // from object.epoch_statistic&.largest_block_size
  private String largest_block; // from EpochStatistic.largest_block_size
  private String cycles;
  private String max_cycles_in_epoch; // from object.epoch_statistic&.max_block_cycles
  private String max_cycles; // from EpochStatistic.max_block_cycles
}
