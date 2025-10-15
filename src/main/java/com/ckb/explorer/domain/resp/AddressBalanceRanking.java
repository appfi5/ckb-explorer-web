package com.ckb.explorer.domain.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressBalanceRanking {

  private String address;

  private String balance;

  private int ranking;
}
