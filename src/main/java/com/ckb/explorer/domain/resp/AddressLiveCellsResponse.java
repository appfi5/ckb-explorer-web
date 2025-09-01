package com.ckb.explorer.domain.resp;

import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressLiveCellsResponse extends BaseResponse<Long> {
  private String type = "cell_output";
  
  private String cellType;

  private String txHash;

  private Integer cellIndex;

  private String typeHash;

  private String data;

  private List<String> tags;
  
  private Long blockNumber;
  
  @JsonSerialize(using = ToStringSerializer.class)
  private BigInteger capacity;
  
  @JsonSerialize(using = ToStringSerializer.class)
  private BigInteger occupiedCapacity;
  
  private Long blockTimestamp;

  private ScriptResponse typeScript;

  private ScriptResponse lockScript;

  private ExtraInfoResponse extraInfo; // TODO 根据不同资产显示不同的信息
}
