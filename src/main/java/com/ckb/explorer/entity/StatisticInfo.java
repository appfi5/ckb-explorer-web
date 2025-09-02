package com.ckb.explorer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ckb.explorer.config.mybatis.AddressBalanceRankingTypeHandler;
import com.ckb.explorer.domain.resp.AddressBalanceRanking;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * StatisticInfo 统计信息实体类
 * 对应数据库表：statistic_infos
 */
@Data
@TableName("statistic_infos")
public class StatisticInfo {

  /**
   * 主键ID
   */
  @TableId(type = IdType.AUTO)
  private Long id;

  /**
   * 过去24小时的交易数量
   */
  @TableField(value = "transactions_last_24hrs")
  private Long transactionsLast24hrs;

  /**
   * 每分钟交易数量
   */
  private Long transactionsCountPerMinute;

  /**
   * 平均区块时间（秒）
   */
  private Double averageBlockTime;

  /**
   * 哈希率（算力）
   */
  private Double hashRate;

  /**
   * 区块链基本信息
   */
  private String blockchainInfo;

  /**
   * 地址余额排名（JSON格式）
   */
  @TableField(typeHandler = AddressBalanceRankingTypeHandler.class)
  private List<AddressBalanceRanking> addressBalanceRanking;

  private Date createdAt;

  private Date updatedAt;
}