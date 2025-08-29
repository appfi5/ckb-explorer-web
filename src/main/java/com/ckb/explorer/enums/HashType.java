package com.ckb.explorer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HashType {

  DATA(0, "data"),
  TYPE(1, "type"),
  DATA1(2, "data1"),
  DATA2(4, "data2");

  private final int code;
  private final String value;

  public static String getValueByCode(Short code){
    if(code == null){
      return null;
    }
    HashType[] hashTypes = HashType.values();
    for (HashType hashType : hashTypes) {
      if(hashType.getCode() == code.intValue()){
        return hashType.getValue();
      }
    }
    return "unknown";
  }

  public static HashType getByCode(Short code){
    if(code == null){
      return null;
    }
    HashType[] hashTypes = HashType.values();
    for (HashType hashType : hashTypes) {
      if(hashType.getCode() == code.intValue()){
        return hashType;
      }
    }
    return null;
  }
}
