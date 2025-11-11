package com.ckb.explorer.domain.dto;


import com.ckb.explorer.domain.resp.TransactionFeeRates;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionFeeRatesWrapper {

  private List<TransactionFeeRates> data;
}
