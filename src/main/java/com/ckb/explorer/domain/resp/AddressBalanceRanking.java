package com.ckb.explorer.domain.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.math.BigInteger;
import lombok.Data;

@Data
public class AddressBalanceRanking {

  private String address;

  @JsonSerialize(using = ToStringSerializer.class)
  private BigInteger balance;

  private int ranking;
}
