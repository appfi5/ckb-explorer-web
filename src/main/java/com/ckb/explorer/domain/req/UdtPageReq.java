package com.ckb.explorer.domain.req;

import com.ckb.explorer.domain.req.base.BasePageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UdtPageReq extends BasePageReq {




    @Schema(description = "排序",example = "typeScriptId.desc | addressesCount.desc | h24CkbTransactionsCount.desc")
    private String sort="typeScriptId.desc";


}
