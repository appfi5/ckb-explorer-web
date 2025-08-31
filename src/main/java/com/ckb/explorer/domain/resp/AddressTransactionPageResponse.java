package com.ckb.explorer.domain.resp;

import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TransactionResponse 用于将 CkbTransaction 实体序列化为 JSON 响应对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressTransactionPageResponse extends BaseResponse<Long> {
  private String type = "ckb_transactions";

  private Boolean isCellbase;

  private String transactionHash;

  private Long blockNumber;

  private Long blockTimestamp;

  private int displayInputsCount;

  private int displayOutputsCount;

  private List<CellInputResponse> displayInputs;

  private List<CellOutputResponse> displayOutputs;

  private BigDecimal income; // TODO 需关联account_books查询

  private Boolean isRgbTransaction; // TODO object.rgb_transaction?

  private Boolean isBtcTimeLock; // TODO object.btc_time_transaction?

  private String rgbTxid; // TODO object.rgb_txid

  private String rgbTransferStep; // TODO object.transfer_step
}