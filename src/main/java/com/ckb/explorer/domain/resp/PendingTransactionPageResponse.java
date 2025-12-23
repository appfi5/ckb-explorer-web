package com.ckb.explorer.domain.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PendingTransactionPageResponse {
  private String type = "ckb_pending_transaction_list";

  private String transactionHash;

  private Long bytes;

  private Long createdAt;

  // (tx.created_at.to_f * 1000).to_i
  private Long createTimestamp;
}
