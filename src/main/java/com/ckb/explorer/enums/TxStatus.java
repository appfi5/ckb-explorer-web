package com.ckb.explorer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.nervos.ckb.Network;

@Getter
@AllArgsConstructor
public enum TxStatus {
  // pending: 0, proposed: 1, committed: 2, rejected: 3
  pending(0, "pending"),
  proposed(1, "proposed"),
  committed(2, "committed"),
  rejected(3, "rejected");


  private final Integer code;
  private final String status;

  public static String getStatusByCode(int code){
    TxStatus[] txStatuses = TxStatus.values();
    for (TxStatus txStatus : txStatuses) {
      if(txStatus.getCode().intValue() == code){
        return txStatus.getStatus();
      }
    }
    return "committed";
  }
}
