package com.ckb.explorer.domain.req;

import com.ckb.explorer.domain.req.base.BasePageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TransactionPageReq extends BasePageReq {

  /**
   * 排序字段
   */
  @Schema(description = "排序字段")
  private String sort;
}
