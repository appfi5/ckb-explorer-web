package com.ckb.explorer.domain.resp.base;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;

@Data
public class BaseResponse<T extends Serializable> implements Serializable {
  @Schema(description = "主键Id")
  @JsonSerialize(using = ToStringSerializer.class)
  private T id;
}
