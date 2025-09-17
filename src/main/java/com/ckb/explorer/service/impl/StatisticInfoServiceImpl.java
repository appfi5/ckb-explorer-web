package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.domain.resp.AddressBalanceRanking;
import com.ckb.explorer.entity.StatisticInfo;
import com.ckb.explorer.mapper.StatisticInfoMapper;
import com.ckb.explorer.service.StatisticInfoService;
import com.ckb.explorer.util.JsonUtil;
import java.util.ArrayList;
import java.util.List;
import org.nervos.ckb.type.BlockchainInfo;
import org.springframework.stereotype.Service;

/**
 * StatisticInfoServiceImpl 统计信息服务实现类
 * 实现statistic_infos表相关的业务逻辑
 */
@Service
public class StatisticInfoServiceImpl extends ServiceImpl<StatisticInfoMapper, StatisticInfo> implements StatisticInfoService {

  @Override
  public List<AddressBalanceRanking> getAddressBalanceRanking() {
    LambdaQueryWrapper<StatisticInfo> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.select(StatisticInfo::getAddressBalanceRanking);
    queryWrapper.last("limit 1");
    StatisticInfo result = baseMapper.selectOne(queryWrapper);

    return result == null ? new ArrayList<>() : result.getAddressBalanceRanking();
  }

  @Override
  public BlockchainInfo getBlockchainInfo() {
    LambdaQueryWrapper<StatisticInfo> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.select(StatisticInfo::getBlockchainInfo)
        .last("LIMIT 1");

    StatisticInfo result = baseMapper.selectOne(queryWrapper);
    String blockchainInfo = result != null ? result.getBlockchainInfo() : null;
    return JsonUtil.parseObject(blockchainInfo, BlockchainInfo.class);
  }

  @Override
  public StatisticInfo getStatisticInfo() {
    LambdaQueryWrapper<StatisticInfo> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.last("limit 1");
    StatisticInfo statisticInfo = baseMapper.selectOne(queryWrapper);
    return statisticInfo;
  }
}