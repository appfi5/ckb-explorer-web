package com.ckb.explorer.domain.resp;

import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * TransactionResponse 用于将 CkbTransaction 实体序列化为 JSON 响应对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionPageResponse extends BaseResponse<Long> {

  private String transactionHash;
  private Long blockNumber;
  private Long blockTimestamp;
  @JsonSerialize(using = ToStringSerializer.class)
  private Long capacityInvolved;
  private Integer liveCellChanges;
}