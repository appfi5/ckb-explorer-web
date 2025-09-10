package com.ckb.explorer.domain.resp;

import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * FungibleTokensPageResponse 用于将 udts page 实体序列化为 JSON 响应对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SudtsPageResponse extends BaseResponse<Long> {

  private String type = "udt";
  private String typeScriptHash;
  private Long blockTimestamp;
  private String iconFile;
  private String fullName;
  private String symbol;
  private Long h24CkbTransactionsCount;
  private Long addressesCount;

}