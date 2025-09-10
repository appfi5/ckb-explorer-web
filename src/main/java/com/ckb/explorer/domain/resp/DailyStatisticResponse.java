package com.ckb.explorer.domain.resp;

import com.ckb.explorer.domain.dto.AddressBalanceDistributionDto;
import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
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

  private Long createdAtUnixtimestamp;// 1

  private String miningReward;// 1

  private String liveCellsCount;// 1

  private String deadCellsCount;// 1

  private String avgHashRate;// 1

  private String avgDifficulty;// 1

  private String uncleRate;// 1

  private BigInteger totalTxFee;// 1

  private Map<String, String> ckbHodlWave; // 1

  private Long holderCount; // 1

  private Map<String, Long> activityAddressContractDistribution;// 1
}
