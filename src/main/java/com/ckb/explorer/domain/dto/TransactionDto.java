package com.ckb.explorer.domain.dto;

import com.ckb.explorer.domain.resp.CellDependencyResponse;
import java.util.List;
import lombok.Data;

@Data
public class TransactionDto {
  private Long id;

  private Boolean isCellbase;

  private String txStatus; // TODO 待确认

  private byte[] witnesses;

  private byte[] headerDeps;

  private String detailedMessage;// if object.tx_status.to_s == "rejected" object.detailed_message

  private String txHash;

  private Long transactionFee;

  private Long blockNumber;

  private String version;

  private Long blockTimestamp;

  private Integer cycles;

  private Long bytes;

  private List<CellDependencyResponse> cellDeps;
}
