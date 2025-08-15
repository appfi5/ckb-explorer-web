package com.ckb.explorer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("account_book")
public class AccountBook {

  @TableId(type = IdType.AUTO)
  private Long id;

  private Long address_id;

  private Long transaction_id;

  private BigDecimal income;
}