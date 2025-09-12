package com.ckb.explorer.domain.resp;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.ArrayTypeHandler;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LiveCellsResponse extends BaseResponse<Long> {

  private Integer cellType;

  private String txHash;

  private Integer cellIndex;

  private String typeHash;

  private String data;

  @TableField(typeHandler = ArrayTypeHandler.class)
  private String[] tags;
  
  private Long blockNumber;
  
  @JsonSerialize(using = ToStringSerializer.class)
  private BigInteger capacity;
  
  @JsonSerialize(using = ToStringSerializer.class)
  private BigInteger occupiedCapacity;
  
  private Long blockTimestamp;

  private ExtraInfoResponse extraInfo; // 根据不同资产 cellType 显示不同的信息
}
