package com.ckb.explorer.domain.dto;

import lombok.Data;

@Data
public class UdtH24TransactionsCountDto {

    private byte[] typeScriptHash;

    private Long h24CkbTransactionsCount;
}
