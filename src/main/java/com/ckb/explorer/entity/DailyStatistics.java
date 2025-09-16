package com.ckb.explorer.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ckb.explorer.config.mybatis.BlockTimeDistributionTypeHandler;
import com.ckb.explorer.config.mybatis.ListStringTypeHandler;
import com.ckb.explorer.config.mybatis.MapTypeHandler;
import com.ckb.explorer.domain.dto.ListStringWrapper;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Data;

/**
 * @TableName daily_statistics
 */
@Data
@TableName(value = "daily_statistics")
public class DailyStatistics implements Serializable {

  private Long id;

  private Long transactionsCount; // 1

  private Long addressesCount;// 1

  private String totalDaoDeposit;// 2

  private Long blockTimestamp;

  private Long createdAtUnixtimestamp;// 1

  private Date createdAt;

  private Date updatedAt;

  private String daoDepositorsCount;

  private String unclaimedCompensation; // dao phase1_dao_interests + unmade_dao_interests

  private String claimedCompensation; // dao

  private String averageDepositTime;

  private String estimatedApc;

  private String miningReward;// 1

  private String depositCompensation;// dao unclaimed_compensation.to_i + claimed_compensation.to_i

  private String treasuryAmount;// 2 dao  burnt:treasury_amount.to_i + MarketData::BURN_QUOTA

  private String liveCellsCount;// 1

  private String deadCellsCount;// 1

  private String avgHashRate;// 1

  private String avgDifficulty;// 1

  private String uncleRate;// 1

  private String totalDepositorsCount;// 2

  private BigInteger totalTxFee;// 1

  @TableField(typeHandler = ListStringTypeHandler.class)
  private ListStringWrapper addressBalanceDistribution; // 1

  private BigInteger occupiedCapacity;

  private BigInteger dailyDaoDeposit;// 2

  private Integer dailyDaoDepositorsCount;// 2

  private BigInteger dailyDaoWithdraw;

  private BigInteger circulationRatio;// 2

  private BigInteger totalSupply;// dao

  private BigInteger circulatingSupply; // dao

  @TableField(typeHandler = BlockTimeDistributionTypeHandler.class)
  private LinkedHashMap<String, String> blockTimeDistribution; // 1

  @TableField(typeHandler = ListStringTypeHandler.class)
  private ListStringWrapper epochTimeDistribution; // 1

  private Object averageBlockTime; // 在别的表查

  private Object nodesDistribution;

  private Integer nodesCount;

  private BigInteger lockedCapacity;// ？？ market_data.ecosystem_locked +
//  market_data.team_locked +
//  market_data.private_sale_locked +
//  market_data.founding_partners_locked +
//  market_data.foundation_reserve_locked +†
//  market_data.bug_bounty_locked

  @TableField(typeHandler = MapTypeHandler.class)
  private Map<String, String> ckbHodlWave; // 1

  private Long holderCount; // 1

  private Long knowledgeSize;// dao

  @TableField(typeHandler = MapTypeHandler.class)
  private Map<String, String> activityAddressContractDistribution;// 1

  private static final long serialVersionUID = 1L;
}