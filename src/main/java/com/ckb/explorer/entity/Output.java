package com.ckb.explorer.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("output")
public class Output {

  @TableId(type = IdType.AUTO)
  private Long id;

  private Long tx_id;

  private byte[] tx_hash;

  private Integer output_index;

  private Long capacity;

  private Long lock_script_id;

  private Long type_script_id;

  private byte[] data;

  private Integer is_spent;

  private byte[] consumed_tx_hash;

  private Integer input_index;

  private Long occupied_capacity;
}