package com.ckb.explorer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ckb_transaction")
public class CkbTransaction {

  @TableId(type = IdType.AUTO)
  private Long id;

  private byte[] tx_hash;

  private byte[] version;

  private Integer input_count;

  private Integer output_count;

  private byte[] witnesses;

  private Long block_id;

  private Long block_number;

  private byte[] block_hash;

  private Integer tx_index;

  private byte[] header_deps;

  private Integer cycles;

  private Long transaction_fee;

  private Long bytes;
}