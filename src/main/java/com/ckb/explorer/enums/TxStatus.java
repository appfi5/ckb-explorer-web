package com.ckb.explorer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.nervos.ckb.Network;

@Getter
@AllArgsConstructor
public enum TxStatus {
  // pending: 0, committed: 1, rejected: 2 (新的实现没有 proposed)
  pending(0, "pending"),
  committed(1, "committed"),
  rejected(2, "rejected");


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
