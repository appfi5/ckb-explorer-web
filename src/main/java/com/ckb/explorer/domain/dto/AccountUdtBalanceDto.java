package com.ckb.explorer.domain.dto;

import com.ckb.explorer.domain.resp.ScriptResponse;
import java.math.BigInteger;
import lombok.Data;

@Data
public class AccountUdtBalanceDto {
  private String symbol;

  private Integer decimal;

  private BigInteger amount;

  private String udtIconFile;

  private Integer udtType;

  private ScriptResponse udtTypeScript;

  private String typeScriptHash;

  private Long totalAmount;
}
