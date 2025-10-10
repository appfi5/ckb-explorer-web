package com.ckb.explorer.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NftItemDto {
    private Long id;

    private Long typeScriptId;

    private Long lockScriptId;

    private String owner;

    private String tokenId;

    private byte[] data;
}
