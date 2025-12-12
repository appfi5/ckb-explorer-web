package com.ckb.explorer.domain.resp;

import com.ckb.explorer.domain.dto.MinerRewardInfo;
import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MinerDailyStatisticsResponse extends BaseResponse<Long> {
  private String type = "miner_daily_statistic";

  private Long createdAtUnixtimestamp;

  private Long maxBlockNumber;

  private Long minBlockNumber;

  // 延迟十一个块的奖励
  private BigDecimal totalReward;

  private String totalHashRate;

  private String avgRor;

  private List<MinerRewardInfo> miners;

}
