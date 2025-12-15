package com.ckb.explorer.domain.dto;

import lombok.Data;

@Data
public class UdtAddressCountDto {

    private Long typeScriptId;

    private Long addressesCount;

    private Long h24CkbTransactionsCount;

    private String typeScriptHash;
}
