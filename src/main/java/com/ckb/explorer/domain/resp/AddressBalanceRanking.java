package com.ckb.explorer.domain.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class AddressBalanceRanking {

  private String address;

  @JsonSerialize(using = ToStringSerializer.class)
  private Long balance;

  private int ranking;
}
