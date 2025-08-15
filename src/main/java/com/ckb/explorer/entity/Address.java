package com.ckb.explorer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("address")
public class Address {

  @TableId(type = IdType.AUTO)
  private Long id;

  private String address;

  private Long script_id;
}