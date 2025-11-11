package com.ckb.explorer.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.Data;

@Data
@TableName(value = "dao_contracts")
public class DaoContract {
  /**
   * 主键 ID
   */
  private Long id;

  /**
   * DAO 总存款（推测为数值类型，用 BigDecimal 确保精度）
   */
  private BigDecimal totalDeposit;

  /**
   * 存款者数量
   */
  private Integer depositorsCount;

  /**
   * 领取的利息（numeric(40) 精度高，用 BigDecimal）
   */
  private BigDecimal claimedCompensation;

  /**
   * 未领取的利息（numeric(40) 精度高，用 BigDecimal）
   */
  private BigDecimal unclaimedCompensation;
}
