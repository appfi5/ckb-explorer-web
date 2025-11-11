package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.domain.resp.UdtDailyStatisticsResponse;
import com.ckb.explorer.entity.UdtDailyStatistics;
import com.ckb.explorer.mapper.UdtDailyStatisticsMapper;
import com.ckb.explorer.service.UdtDailyStatisticsService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UdtDailyStatisticsServiceImpl extends
    ServiceImpl<UdtDailyStatisticsMapper, UdtDailyStatistics> implements
    UdtDailyStatisticsService {

  @Override
  public List<UdtDailyStatisticsResponse> getUdtDailyStatistics() {
    return baseMapper.getUdtDailyStatistics();
  }
}
