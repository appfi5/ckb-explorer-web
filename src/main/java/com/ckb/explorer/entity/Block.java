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

  private byte[] block_hash;

  private Long block_number;

  private byte[] compact_target;

  private byte[] parent_hash;

  private byte[] nonce;

  private byte[] difficulty;

  private Long timestamp;

  private byte[] version;

  private byte[] transactions_root;

  private Integer transactions_count;

  private byte[] epoch;

  private Long start_number;

  private Integer epoch_length;

  private Long epoch_number;

  private byte[] dao;

  private byte[] proposals_hash;

  private byte[] extra_hash;

  private byte[] extension;

  private byte[] proposals;

  private Integer proposals_count;

  private Integer uncles_count;

  private byte[] uncle_block_hashes;

  private byte[] miner_script;

  private String miner_message;

  private Long reward;

  private Long total_transaction_fee;

  private Long cell_consumed;

  private Long total_cell_capacity;

  private Integer block_size;

  private Long cycles;

  private Integer live_cell_changes;
}