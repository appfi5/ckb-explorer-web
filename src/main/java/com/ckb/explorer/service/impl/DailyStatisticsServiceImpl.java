package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.domain.resp.DailyStatisticResponse;
import com.ckb.explorer.entity.DailyStatistics;
import com.ckb.explorer.mapper.DailyStatisticsMapper;
import com.ckb.explorer.mapstruct.DailyStatisticsConvert;
import com.ckb.explorer.service.DailyStatisticsService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * DailyStatisticsServiceImpl 实现了 DailyStatisticsService 接口，提供每日统计数据相关的具体业务实现
 */
@Service
public class DailyStatisticsServiceImpl extends
    ServiceImpl<DailyStatisticsMapper, DailyStatistics> implements DailyStatisticsService {

  @Override
  public List<DailyStatisticResponse> getByIndicator(String indicator) {
    LambdaQueryWrapper<DailyStatistics> queryWrapper = new LambdaQueryWrapper<>();
    
    // 创建一个List来收集所有需要查询的字段
    List<SFunction<DailyStatistics, ?>> fields = new ArrayList<>();
    
    // 添加基本字段
    fields.add(DailyStatistics::getCreatedAtUnixtimestamp);
    fields.add(DailyStatistics::getId);
    
    // 根据指标名称添加额外需要查询的字段
    String[] parts = indicator.split("-");
    for (String part : parts) {
      switch (part) {
        case "transactions_count":
          fields.add(DailyStatistics::getTransactionsCount);
          break;
        case "addresses_count":
          fields.add(DailyStatistics::getAddressesCount);
          break;
        case "live_cells_count":
          fields.add(DailyStatistics::getLiveCellsCount);
          break;
        case "dead_cells_count":
          fields.add(DailyStatistics::getDeadCellsCount);
          break;
        case "avg_hash_rate":
          fields.add(DailyStatistics::getAvgHashRate);
          break;
        case "avg_difficulty":
          fields.add(DailyStatistics::getAvgDifficulty);
          break;
        case "uncle_rate":
          fields.add(DailyStatistics::getUncleRate);
          break;
        case "total_tx_fee":
          fields.add(DailyStatistics::getTotalTxFee);
          break;
        case "mining_reward":
          fields.add(DailyStatistics::getMiningReward);
          break;
        case "ckb_hodl_wave":
          fields.add(DailyStatistics::getCkbHodlWave);
          break;
        case "holder_count":
          fields.add(DailyStatistics::getHolderCount);
          break;
        case "activity_address_contract_distribution":
          fields.add(DailyStatistics::getActivityAddressContractDistribution);
          break;
        default:
          break;
      }
    }
    
    // 将所有字段设置到查询条件中
    queryWrapper.select(fields.toArray(new SFunction[0]));

    // 设置排序
    queryWrapper.orderByAsc(DailyStatistics::getCreatedAtUnixtimestamp);
    var result = baseMapper.selectList(queryWrapper);
    // 根据不同的指标名称实现不同的查询逻辑
    return DailyStatisticsConvert.INSTANCE.toConvertList(result);

  }
}