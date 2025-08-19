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

  private Long outputId;

  private byte[] preOutpointTxHash;

  private Integer preOutpointIndex;

  private byte[] since;

  private Long consumedTxId;

  private byte[] consumedTxHash;

  private Integer inputIndex;
}