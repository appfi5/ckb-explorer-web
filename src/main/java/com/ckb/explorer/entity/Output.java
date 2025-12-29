package com.ckb.explorer.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigInteger;
import lombok.Data;

@Data
@TableName("output")
public class Output {

  @TableId(type = IdType.AUTO)
  private Long id;

  private Long txId;

  private byte[] txHash;

  private Integer outputIndex;

  private BigInteger capacity;

  private Long lockScriptId;

  private Long typeScriptId;

  private byte[] data;

  private Integer dataSize;

  private byte[] dataHash;

  private Integer isSpent;

  private byte[] consumedTxHash;

  private Integer inputIndex;

  private BigInteger occupiedCapacity;

  private Long blockNumber;

  private Long blockTimestamp;

  private Long consumedBlockNumber;

  private Long consumedTimestamp;
}