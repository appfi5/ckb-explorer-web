package com.ckb.explorer.domain.resp;

import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DailyStatisticResponse extends BaseResponse<Long> {
  private String type = "daily_statistic";

  private Long transactionsCount; // 1

  private Long addressesCount;// 1

  private String totalDaoDeposit;// 2

  private Long createdAtUnixtimestamp;// 1

  private String miningReward;// 1

  private String depositCompensation;// dao

  private String treasuryAmount;// 2 dao

  private String liveCellsCount;// 1

  private String deadCellsCount;// 1

  private String avgHashRate;// 1

  private String avgDifficulty;// 1

  private String uncleRate;// 1

  private String totalDepositorsCount;// 2

  @JsonSerialize(using = ToStringSerializer.class)
  private BigInteger totalTxFee;// 1

  @JsonSerialize(using = ToStringSerializer.class)
  private BigInteger dailyDaoDeposit;// 2

  @JsonSerialize(using = ToStringSerializer.class)
  private Integer dailyDaoDepositorsCount;// 2

  @JsonSerialize(using = ToStringSerializer.class)
  private BigDecimal circulationRatio;// 2

  @JsonSerialize(using = ToStringSerializer.class)
  private BigDecimal circulatingSupply; // dao

  @JsonSerialize(using = ToStringSerializer.class)
  private BigDecimal burnt;

  @JsonSerialize(using = ToStringSerializer.class)
  private BigInteger lockedCapacity;

  @JsonSerialize(using = ToStringSerializer.class)
  private BigDecimal liquidity ; // dao

  private Map<String, String> ckbHodlWave; // 1

  private Long holderCount; // 1

  @JsonSerialize(using = ToStringSerializer.class)
  private BigInteger knowledgeSize;// dao

  private Map<String, String> activityAddressContractDistribution;// 1
}
