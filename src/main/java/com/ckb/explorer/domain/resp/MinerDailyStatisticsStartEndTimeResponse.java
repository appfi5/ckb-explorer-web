package com.ckb.explorer.domain.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MinerDailyStatisticsStartEndTimeResponse {

  /**
   * 矿工的统计数据开始时间  UTC时间
   */
  private Long startTime;

  /**
   * 矿工的统计数据结束时间 UTC时间
   */
  private Long endTime;
}
