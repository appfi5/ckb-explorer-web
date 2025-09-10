package com.ckb.explorer.domain.req;

import com.ckb.explorer.domain.req.base.BasePageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SudtsPageReq extends BasePageReq {



    @Schema(description = "排序字段")
    private String sort;

    private  boolean union = true;

}
