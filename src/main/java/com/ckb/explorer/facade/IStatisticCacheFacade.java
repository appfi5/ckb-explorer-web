package com.ckb.explorer.facade;

import com.ckb.explorer.domain.resp.IndexStatisticResponse;
import com.ckb.explorer.domain.resp.StatisticResponse;

public interface IStatisticCacheFacade {
  IndexStatisticResponse getIndexStatistic();

  StatisticResponse getStatisticByFieldName(String fieldName);
}
