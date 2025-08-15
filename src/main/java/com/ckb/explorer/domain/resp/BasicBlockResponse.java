package com.ckb.explorer.domain.resp;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class BasicBlockResponse {
    private Long id;
    private Integer version;
    private BigDecimal compact_target;
    private Long block_timestamp;
    private Long number;
    private Long epoch;
    private String parent_hash;
    private String transactions_root;
    private String proposals_hash;
    private String extra_hash;
    private String dao;
    private String extension;
    private BigDecimal nonce;
    private String block_hash;
    private List<String> proposals;
    private Integer uncles_count;
}