package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.domain.resp.MinerDailyStatisticsResponse;
import com.ckb.explorer.domain.resp.MinerDailyStatisticsStartEndTimeResponse;
import com.ckb.explorer.entity.MinerDailyStatistics;
import com.ckb.explorer.mapper.MinerDailyStatisticsMapper;
import com.ckb.explorer.mapstruct.MinerDailyStatisticsConvert;
import com.ckb.explorer.service.MinerDailyStatisticsService;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * MinerDailyStatisticsServiceImpl
 */
@Service
@Slf4j
public class MinerDailyStatisticsServiceImpl extends
    ServiceImpl<MinerDailyStatisticsMapper, MinerDailyStatistics> implements MinerDailyStatisticsService {

  /**
   * 获取近30天的avgROR数据
   * @return
   */
  @Override
  public List<MinerDailyStatisticsResponse> getAvgRor() {

    return baseMapper.getAvgRor();
  }

  /**
   * 获取已统计的开始结束时间
   */
  @Override
  public MinerDailyStatisticsStartEndTimeResponse getStartEndTime() {
    return baseMapper.getStartEndTime();
  }

  /**
   * 获取指定日期的统计数据
   */
  @Override
  public MinerDailyStatisticsResponse getByDate(LocalDate date) {
    Long dateLong = date.atStartOfDay(ZoneOffset.UTC)
        .toInstant()
        .getEpochSecond();
    MinerDailyStatistics minerDailyStatistics = baseMapper.getByDate(dateLong);
    return MinerDailyStatisticsConvert.INSTANCE.toConvert(minerDailyStatistics);
  }
}
