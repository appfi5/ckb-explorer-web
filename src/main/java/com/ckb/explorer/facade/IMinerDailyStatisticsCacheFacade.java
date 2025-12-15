package com.ckb.explorer.facade;

import com.ckb.explorer.domain.resp.MinerDailyStatisticsResponse;
import com.ckb.explorer.domain.resp.MinerDailyStatisticsStartEndTimeResponse;
import java.time.LocalDate;
import java.util.List;

public interface IMinerDailyStatisticsCacheFacade {

  List<MinerDailyStatisticsResponse> getAvgRor();

  MinerDailyStatisticsStartEndTimeResponse getStartEndTime();

  MinerDailyStatisticsResponse getByDate(LocalDate date);
}
