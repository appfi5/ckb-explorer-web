package com.ckb.explorer.domain.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LastNDaysTransactionFeeRates {

  private String date;
  private String feeRate;
}
