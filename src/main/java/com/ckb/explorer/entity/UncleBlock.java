package com.ckb.explorer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("uncle_block")
public class UncleBlock {

  @TableId(type = IdType.AUTO)
  private Long id;

  private Long block_id;

  private Integer index;

  private byte[] block_hash;

  private Long block_number;

  private byte[] compact_target;

  private byte[] parent_hash;

  private byte[] nonce;

  private Long timestamp;

  private byte[] version;

  private byte[] transactions_root;

  private byte[] epoch;

  private byte[] dao;

  private byte[] proposals_hash;

  private byte[] extra_hash;

  private byte[] extension;

  private byte[] proposals;
}