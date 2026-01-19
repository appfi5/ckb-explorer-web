package com.ckb.explorer.service.impl;

import com.ckb.explorer.entity.RollingAvgBlockTime;
import com.ckb.explorer.mapper.RollingAvgBlockTimeMapper;
import com.ckb.explorer.service.AverageBlockTimeService;
import jakarta.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * AverageBlockTimeServiceImpl 平均区块时间服务实现类 实现平均区块时间相关的服务方法
 */
@Service
@Slf4j
public class AverageBlockTimeServiceImpl implements AverageBlockTimeService {

  @Resource
  private RollingAvgBlockTimeMapper rollingAvgBlockTimeMapper;

  @Override
  public List<RollingAvgBlockTime> getAvgBlockTime(Integer limit) {
    var list = rollingAvgBlockTimeMapper.findOrderedWithLimit(limit);
    // 按时间正序排序返回
    return list.stream().sorted(Comparator.comparing(RollingAvgBlockTime::getTimestamp)).collect(
        Collectors.toList());
  }
}