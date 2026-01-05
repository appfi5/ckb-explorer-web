package com.ckb.explorer.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigInteger;
import lombok.Data;

@Data
@TableName("output")
public class PendingOutput {

  private byte[] txHash;

  private Integer outputIndex;

  private BigInteger capacity;

  private byte[] lockCodeHash;

  private Short lockHashType;

  private byte[] lockArgs;

  private byte[] lockScriptHash;

  private byte[] typeCodeHash;

  private Short typeHashType;

  private byte[] typeArgs;

  private byte[] typeScriptHash;

  private byte[] data;

  private Integer dataSize;

  private byte[] dataHash;

  private BigInteger occupiedCapacity;
}