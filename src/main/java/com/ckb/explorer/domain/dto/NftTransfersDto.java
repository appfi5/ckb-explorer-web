package com.ckb.explorer.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NftTransfersDto {

    private Long id;

    private byte[] data;

    private Long lockScriptId;

    private Long ftLockScriptId;

    private byte[] txHash;

    private Integer isSpent;

    private Long blockTimestamp;






}
