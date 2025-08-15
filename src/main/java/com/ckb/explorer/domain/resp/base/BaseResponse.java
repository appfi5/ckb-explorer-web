package com.ckb.explorer.domain.resp.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BaseResponse<T> {
  @Schema(description = "主键Id")
  private String id;

  @Schema(description = "类型")
  private String type;

  private T attributes;
}
