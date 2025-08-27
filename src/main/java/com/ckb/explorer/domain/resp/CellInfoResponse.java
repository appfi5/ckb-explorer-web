package com.ckb.explorer.domain.resp;

import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.math.BigInteger;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CellInfoResponse
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class CellInfoResponse {
  private LockScriptResponse lockScript;

  private TypeScriptResponse typeScript;

  private String data;

}