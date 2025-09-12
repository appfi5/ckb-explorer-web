package com.ckb.explorer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.math.BigInteger;
import lombok.Data;

/**
 * StatisticAddress 地址统计信息实体类 对应数据库表：statistic_address
 */
@Data
@TableName("statistic_address")
public class StatisticAddress {

  /**
   * 主键ID
   */
  @TableId(type = IdType.AUTO)
  private Long id;

  /**
   * 锁脚本ID
   */
  private Long lockScriptId;

  /**
   * 脚本哈希
   */
  private byte[] scriptHash;

  /**
   * 地址余额
   */
  private BigDecimal balance;

  /**
   * 存活Cell数量
   */
  private Long liveCellsCount;

  /**
   * 占用的余额
   */
  private BigDecimal balanceOccupied;
}
