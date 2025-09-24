package com.ckb.explorer.domain.dto;

import com.ckb.explorer.domain.resp.ScriptResponse;
import java.math.BigInteger;
import lombok.Data;

@Data
public class AccountUdtBalanceDto {

  private BigInteger amount;

  private Long typeScriptId;
}
