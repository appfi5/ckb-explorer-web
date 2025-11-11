package com.ckb.explorer.domain.resp;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionFeeRatesResponse {

  List<TransactionFeeRates> transactionFeeRates;

  List<LastNDaysTransactionFeeRates> lastNDaysTransactionFeeRates;
}
