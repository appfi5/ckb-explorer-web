package com.ckb.explorer.domain.req;

import com.ckb.explorer.domain.req.base.BasePageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class XudtsPageReq extends BasePageReq {


    @Schema(description = "符号/名称")
    private String symbol;

    @Schema(description = "tags",example = "tags to filter tokens, availables are \"rgbpp-compatible\", \"layer-1-asset\", \"layer-2-asset\", \"supply-limited\", \"supply-unlimited\", \"suspicious\", \"invalid\"")
    private String tags;

    @Schema(description = "排序字段")
    private String sort;

    private  boolean union = true;

}
