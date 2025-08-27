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

  private String type = "ckb_transactions";

  private Boolean isCellbase;

  private String txStatus = TxStatus.committed.getStatus(); // TODO 第一版没有其他状态的交易

  private List<String> witnesses;

  private List<CellDependencyResponse> cellDeps;

  private List<String> headerDeps;

  private String detailedMessage;// TODO if object.tx_status.to_s == "rejected" object.detailed_message

  private String transactionHash;

  private Long transactionFee;

  private Long blockNumber;

  private String version;

  private Long blockTimestamp;

  private Long bytes;

  private Long largestTxInEpoch;// TODO object.block&.epoch_statistic&.largest_tx_bytes

  private Long largestTx; // TODO EpochStatistic.largest_tx_bytes

  private Long cycles;

  private Long maxCyclesInEpoch; // TODO object.block&.epoch_statistic&.max_tx_cycles

  private Long max_cycles; // TODO EpochStatistic.max_tx_cycles

  private Boolean isRgbTransaction; // TODO object.rgb_transaction?

  private Boolean isBtcTimeLock; // TODO object.btc_time_transaction?

  private String rgbTxid; // TODO object.rgb_txid

  private String rgbTransferStep; // TODO object.transfer_step
}
