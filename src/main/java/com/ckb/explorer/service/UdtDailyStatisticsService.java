package com.ckb.explorer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ckb.explorer.domain.resp.UdtDailyStatisticsResponse;
import com.ckb.explorer.entity.UdtDailyStatistics;
import java.util.List;

public interface UdtDailyStatisticsService extends IService<UdtDailyStatistics> {
  List<UdtDailyStatisticsResponse> getUdtDailyStatistics();
}
