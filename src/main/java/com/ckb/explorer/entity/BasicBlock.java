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


    private BigDecimal compactTarget;

    private Long blockTimestamp;

    private Long number;

    private Long epoch;

    private byte[] parentHash;

    private byte[] transactionsRoot;

    private byte[] proposalsHash;

    private byte[] extraHash;

    private String dao;

    private byte[] extension;

    private BigDecimal nonce;

    private byte[] blockHash;

    private byte[] proposals;

    private Integer unclesCount;
}