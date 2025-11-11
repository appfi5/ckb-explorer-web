package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.domain.resp.EpochStatisticsResponse;
import com.ckb.explorer.entity.DailyStatistics;
import com.ckb.explorer.entity.EpochStatistics;
import com.ckb.explorer.mapper.DailyStatisticsMapper;
import com.ckb.explorer.mapper.EpochStatisticsMapper;
import com.ckb.explorer.mapstruct.EpochStatisticsConvert;
import com.ckb.explorer.service.EpochStatisticsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * EpochStatisticsServiceImpl 实现EpochStatisticsService接口，提供纪元统计数据的查询逻辑
 */
@Service
@Transactional(readOnly = true)
public class EpochStatisticsServiceImpl extends
    ServiceImpl<EpochStatisticsMapper, EpochStatistics> implements EpochStatisticsService {

    @Override
    public List<EpochStatisticsResponse> getEpochStatistics(Integer limit, String indicator) {
      LambdaQueryWrapper<EpochStatistics> queryWrapper = new LambdaQueryWrapper<>();
      // 创建一个List来收集所有需要查询的字段
      List<SFunction<EpochStatistics, ?>> fields = new ArrayList<>();
      fields.add(EpochStatistics::getEpochNumber);
      fields.add(EpochStatistics::getLargestBlockNumber);
      fields.add(EpochStatistics::getLargestBlockSize);
      fields.add(EpochStatistics::getLargestTxBytes);
      fields.add(EpochStatistics::getLargestTxHash);

      String[] parts = indicator.split("-");
      for (String part : parts) {
        switch (part) {
          case "difficulty":
            fields.add(EpochStatistics::getDifficulty);
            break;
          case "uncle_rate":
            fields.add(EpochStatistics::getUncleRate);
            break;
          case "hash_rate":
            fields.add(EpochStatistics::getHashRate);
            break;
          case "epoch_time":
            fields.add(EpochStatistics::getEpochTime);
            break;
          case "epoch_length":
            fields.add(EpochStatistics::getEpochLength);
          }
        }
      queryWrapper.select(fields.toArray(new SFunction[0]));
        if (limit != null) {
          queryWrapper.orderByDesc(EpochStatistics::getEpochNumber);
        } else {
          queryWrapper.orderByAsc(EpochStatistics::getEpochNumber);
        }
      var result = baseMapper.selectList(queryWrapper);
        return EpochStatisticsConvert.INSTANCE.toConvertList(result);
    }
}