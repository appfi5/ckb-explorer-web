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

  private byte[] txHash;

  private byte[] version;

  private Integer inputCount;

  private Integer outputCount;

  private byte[] witnesses;

  private Long blockId;

  private Long blockNumber;

  private byte[] blockHash;

  private Long blockTimestamp;

  private Integer txIndex;

  private byte[] headerDeps;

  // 注意交易的bytes跟旧浏览器差了4字节，展示时需要加回去，如果计算费率的话，需要加8字节
  private Long cycles;

  private Long transactionFee;

  private Long bytes;

  private Long capacityInvolved;
}