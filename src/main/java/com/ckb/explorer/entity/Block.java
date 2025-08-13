package com.ckb.explorer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("block")
public class Block {

  @TableId(type = IdType.AUTO)
  private Long id;

  private byte[] blockHash;

  private Long blockNumber;

  private byte[] compactTarget;

  private byte[] parentHash;

  private byte[] nonce;

  private Long difficulty;

  private Long timestamp;

  private byte[] version;

  private byte[] transactionsRoot;

  private Integer transactionsCount;

  private byte[] epoch;

  private Long startNumber;

  private Integer length;

  private Long epochNumber;

  private byte[] dao;

  private byte[] proposalsHash;

  private byte[] extraHash;

  private byte[] extension;

  private byte[] proposals;

  private Integer proposalsCount;

  private Integer unclesCount;

  private byte[] uncleBlockHashes;

  private byte[] minerHash;

  private String minerMessage;

  private Long reward;

  private Long receivedTxFee;

  private Long totalTransactionFee;

  private Long cellConsumed;

  private Long totalCellCapacity;

  private Integer blockSize;

  private Long cycles;

  private Integer liveCellChanges;
}