package com.ckb.explorer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.nervos.ckb.Network;

@Getter
@AllArgsConstructor
public enum NetWorkEnums {
  MAINNET(1, Network.MAINNET),
  TESTNET(2, Network.TESTNET);


  private final Integer code;
  private final Network net;

  public static Network getNet(int code){
    NetWorkEnums[] netWorkEnums = NetWorkEnums.values();
    for (NetWorkEnums netWork : netWorkEnums) {
      if(netWork.getCode().intValue() == code){
        return netWork.getNet();
      }
    }
    return Network.TESTNET;
  }
}
