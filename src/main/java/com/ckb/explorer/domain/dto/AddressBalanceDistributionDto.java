package com.ckb.explorer.domain.dto;

import java.math.BigInteger;
import lombok.Data;

@Data
public class AddressBalanceDistributionDto {

  private BigInteger rangeUpper;

  private BigInteger addressCount;

  private BigInteger cumulativeCount;
}
