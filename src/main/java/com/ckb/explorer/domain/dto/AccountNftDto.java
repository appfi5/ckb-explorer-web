package com.ckb.explorer.domain.dto;

import lombok.Data;

@Data
public class AccountNftDto {

    private String collectionName;

    private byte[] dobScriptHash;

    private byte[] dobCodeScriptArgs;

    private byte[] data;

    private Long cellId;

}
