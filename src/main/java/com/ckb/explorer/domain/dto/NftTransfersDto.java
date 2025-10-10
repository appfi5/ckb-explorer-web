package com.ckb.explorer.domain.dto;

import com.ckb.explorer.entity.Script;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NftTransfersDto {

    private Long id;

    private Long typeScriptId;

    private Long lockScriptId;

    private Long ftLockScriptId;

    private byte[] txHash;

    private Integer isSpent;

    private Long blockTimestamp;

    private String action;

    private String from;

    private String to;

    private String tokenId;






}
