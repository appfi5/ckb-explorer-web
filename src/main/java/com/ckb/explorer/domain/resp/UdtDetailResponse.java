package com.ckb.explorer.domain.resp;

import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * UdtDetailResponse 用于将 Udts 实体序列化为 JSON 响应对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UdtDetailResponse extends BaseResponse<Long> {

  private String type = "udt";
  private String symbol;
  private String fullName;
  private String iconFile;
  private Boolean published;
  private String description;
  private String typeScriptHash;
  private ScriptResponse typeScript;
  private String issuerAddress;
  private String udtType;
  private String operatorWebsite;
  private Long totalAmount;
  private Long addressesCount;
  private Integer decimal;
  private Long h24CkbTransactionsCount;
  private Long holdersCount;
  private Long blockTimestamp;
  private String[] xudtTags;


}