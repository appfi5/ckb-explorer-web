package com.ckb.explorer.domain.req;

import com.ckb.explorer.domain.req.base.BasePageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AddressLiveCellsPageReq extends BasePageReq {

  @Schema(description = "标签")
  private String tag;

  /**
   * 排序字段
   */
  @Schema(description = "排序字段")
  private String sort;

  @Schema(description = "绑定状态")
  private Boolean boundStatus = false;
}
