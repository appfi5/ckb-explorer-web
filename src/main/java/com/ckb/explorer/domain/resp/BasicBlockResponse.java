package com.ckb.explorer.domain.resp;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class BasicBlockResponse {
    private Long id;
    private Integer version;
    private BigDecimal compactTarget;
    private Long blockTimestamp;
    private Long number;
    private Long epoch;
    private String parentHash;
    private String transactionsRoot;
    private String proposalsHash;
    private String extraHash;
    private String dao;
    private String extension;
    private BigDecimal nonce;
    private String blockHash;
    private List<String> proposals;
    private Integer unclesCount;
}