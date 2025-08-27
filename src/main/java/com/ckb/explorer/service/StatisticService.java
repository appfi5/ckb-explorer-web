package com.ckb.explorer.service;

import com.ckb.explorer.domain.resp.IndexStatisticResponse;
import com.ckb.explorer.domain.resp.StatisticResponse;
import java.util.Map;

/**
 * 统计服务接口
 */
public interface StatisticService {

  double hashRate(Long tipBlockNumber);

  double getAverageBlockTime(Long tipBlockNumber, Long timestamp);

  Long getTransactionsLast24hrs(Long timestamp);

  Long getTransactionsCountPerMinute(Long tipBlockNumber);
}