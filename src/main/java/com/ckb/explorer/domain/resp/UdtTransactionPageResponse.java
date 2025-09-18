package com.ckb.explorer.domain.resp;

import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * UdtTransactionPageResponse 用于将 CkbTransaction 实体序列化为 JSON 响应对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UdtTransactionPageResponse extends BaseResponse<Long> {
  private String type = "ckb_transactions";

  private Boolean isCellbase;

  private String transactionHash;

  private Long blockNumber;

  private Long blockTimestamp;

  private int displayInputsCount;

  private int displayOutputsCount;

  private List<CellInputResponse> displayInputs;

  private List<CellOutputResponse> displayOutputs;

  // private BigDecimal income; // 不展示

  // private Boolean isRgbTransaction; // object.rgb_transaction 一期不做rgb++

  // private Boolean isBtcTimeLock; // object.btc_time_transaction 一期不做比特币

  // private String rgbTxid; // object.rgb_txid 一期不做rgb++

  // private String rgbTransferStep; // object.transfer_step 一期不做rgb++
}