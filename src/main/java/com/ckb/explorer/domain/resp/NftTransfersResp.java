package com.ckb.explorer.domain.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NftTransfersResp {

    private Long id;

    @Schema(description="tokenId")
    private String tokenId;

    @Schema(description="from地址hash")
    private String from;

    @Schema(description="to地址hash")
    private String to;

    @Schema(description="action",example = "Mint、Burn、Transfer")
    private  String action;

    @Schema(description="交易hash")
    private String txHash;

    @Schema(description="交易时间")
    private Long blockTimestamp;

    @Schema(description="cell data")
    private String data;

    private String standard;

    private String iconUrl;






}
