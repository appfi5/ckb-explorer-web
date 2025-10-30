package com.ckb.explorer.domain.resp;

import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.ckb.explorer.enums.TxStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * TransactionResponse 用于将 CkbTransaction 实体序列化为 JSON 响应对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionResponse extends BaseResponse<Long> {

  private String type = "ckb_transaction";

  private Boolean isCellbase;

  private String txStatus = TxStatus.committed.getStatus(); //  第一版没有其他状态的交易

  private List<String> witnesses;

  private List<CellDependencyResponse> cellDeps;

  private List<String> headerDeps;

  private String detailedMessage;//  if object.tx_status.to_s == "rejected" object.detailed_message

  private String transactionHash;

  private Long transactionFee;

  private Long blockNumber;

  private String version;

  private Long blockTimestamp;

  private Long bytes;

  private Long largestTxInEpoch;// object.block&.epoch_statistic&.largest_tx_bytes 一期不做epoch统计

  private Long largestTx; // EpochStatistic.largest_tx_bytes  一期不做epoch统计

  private Long cycles;

  private Long maxCyclesInEpoch; // object.block&.epoch_statistic&.max_tx_cycles  一期不做epoch统计

  private Long maxCycles; // EpochStatistic.max_tx_cycles  一期不做epoch统计

  private Boolean isRgbTransaction; // object.rgb_transaction? 一期不做rgb++

  private Boolean isBtcTimeLock; // object.btc_time_transaction? 一期不做比特币

  private String rgbTxid; //  object.rgb_txid 一期不做rgb++

  private String rgbTransferStep; // object.transfer_step 一期不做rgb++
}
