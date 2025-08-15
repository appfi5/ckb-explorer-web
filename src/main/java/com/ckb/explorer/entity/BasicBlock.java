package com.ckb.explorer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("basic_block")
public class BasicBlock {

  @TableId(type = IdType.AUTO)
    private Long id;

    private Integer version;


    private BigDecimal compact_target;

    private Long block_timestamp;

    private Long number;

    private Long epoch;

    private byte[] parent_hash;

    private byte[] transactions_root;

    private byte[] proposals_hash;

    private byte[] extra_hash;

    private String dao;

    private byte[] extension;

    private BigDecimal nonce;

    private byte[] block_hash;

    private byte[] proposals;

    private Integer uncles_count;
}