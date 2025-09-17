package com.ckb.explorer.domain.dto;

import lombok.Data;

@Data
public class UdtAddressCountDto {

    private byte[] typeScriptHash;

    private Long addressesCount;
}
