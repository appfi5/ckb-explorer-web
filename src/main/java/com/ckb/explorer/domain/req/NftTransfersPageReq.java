package com.ckb.explorer.domain.req;

import com.ckb.explorer.domain.req.base.BasePageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class NftTransfersPageReq extends BasePageReq {


    @Schema(description = "交易哈希")
    private String txHash;


    @Schema(description = "地址哈希")
    private String addressHash;


    @Schema(description = "cellId")
    private Long cellId;


}
