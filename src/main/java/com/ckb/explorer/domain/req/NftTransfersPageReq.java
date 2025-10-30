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


    @Schema(description = "tokenId")
    private String tokenId;

    @Schema(description="action",example = "Mint、Burn、Transfer")
    private  String action;

}
