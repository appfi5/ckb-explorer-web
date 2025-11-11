package com.ckb.explorer.domain.req;

import com.ckb.explorer.domain.req.base.BasePageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CollectionsPageReq extends BasePageReq {


    @Schema(description = "tags",example = "tags to filter tokens, availables are \"invalid\", \"suspicious\", \"out-of-length-range\", \"rgb++\", \"layer-1-asset\", \"layer-2-asset\", \"supply-limited\"")
    private String tags;


    @Schema(description = "排序",example = "block_timestamp.desc")
    private String sort="block_timestamp.desc";


}
