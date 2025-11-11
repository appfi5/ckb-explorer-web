package com.ckb.explorer.facade;

import com.ckb.explorer.domain.resp.UdtDailyStatisticsResponse;
import java.util.List;

public interface IUdtDailyStatisticsCacheFacade {
  List<UdtDailyStatisticsResponse> index();
}
