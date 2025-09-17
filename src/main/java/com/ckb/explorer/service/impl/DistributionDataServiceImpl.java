package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ckb.explorer.domain.resp.DistributionDataResponse;
import com.ckb.explorer.entity.DailyStatistics;
import com.ckb.explorer.mapper.DailyStatisticsMapper;
import com.ckb.explorer.service.DistributionDataService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * DistributionDataServiceImpl 实现DistributionDataService接口，提供分布数据相关的具体业务实现
 */
@Service
public class DistributionDataServiceImpl implements DistributionDataService {

  @Resource
  private DailyStatisticsMapper dailyStatisticsMapper;

  @Override
  public DistributionDataResponse getDistributionDataByIndicator(String indicator) {
    LambdaQueryWrapper<DailyStatistics> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.orderByDesc(DailyStatistics::getCreatedAtUnixtimestamp);
    queryWrapper.last("limit 1");
    var dailyStatistics = dailyStatisticsMapper.selectOne(queryWrapper);

    if (dailyStatistics == null) {
      return null;
    }

    var result = new DistributionDataResponse();
    result.setId(dailyStatistics.getId());
    String[] parts = indicator.split("-");
    for (String part : parts) {
      switch (part) {
        case "address_balance_distribution":
          result.setAddressBalanceDistribution(
              dailyStatistics.getAddressBalanceDistribution().getData());
          break;
        case "block_time_distribution":
          result.setBlockTimeDistribution(dailyStatistics.getBlockTimeDistribution().getData());
          break;
        case "epoch_time_distribution":
          result.setEpochTimeDistribution(dailyStatistics.getEpochTimeDistribution().getData());
          break;
//              case "epoch_length_distribution":
//              result.setEpochLengthDistribution(dailyStatistics.getEpochLengthDistribution());
//              break;
//              case "nodes_distribution":
//              result.setNodesDistribution(dailyStatistics.getNodesDistribution());
//              break;
//              case "miner_address_distribution":
//              result.setMinerAddressDistribution(dailyStatistics.getMinerAddressDistribution());
//              break;
        default:
          break;
      }
    }

    return result;
  }
}