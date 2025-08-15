package com.ckb.explorer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("script")
public class Script {

  @TableId(type = IdType.AUTO)
  private Long id;

  private byte[] code_hash;

  private Short hash_type;

  private byte[] args;

  private byte[] script_hash;
}