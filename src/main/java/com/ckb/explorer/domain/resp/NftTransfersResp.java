package com.ckb.explorer.domain.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NftTransfersResp {

    private Long id;

    private String tokenId;

    private String from;

    private String to;

    private  String action;

    private String txHash;

    private Long blockTimestamp;






}
