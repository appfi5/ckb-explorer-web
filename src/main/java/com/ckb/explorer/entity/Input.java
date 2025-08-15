package com.ckb.explorer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("input")
public class Input {

  @TableId(type = IdType.AUTO)
  private Long id;

  private Long output_id;

  private byte[] pre_outpoint_tx_hash;

  private Integer pre_outpoint_index;

  private byte[] since;

  private Long consumed_tx_id;

  private byte[] consumed_tx_hash;

  private Integer input_index;
}