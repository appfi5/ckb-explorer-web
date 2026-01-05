package com.ckb.explorer.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("input")
public class PendingInput {

  private byte[] txHash;

  private Integer inputIndex;

  private byte[] preOutpointTxHash;

  private Integer preOutpointIndex;

  private byte[] since;

}