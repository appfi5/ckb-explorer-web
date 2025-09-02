package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.domain.resp.AddressBalanceRanking;
import com.ckb.explorer.entity.StatisticInfo;
import com.ckb.explorer.mapper.StatisticInfoMapper;
import com.ckb.explorer.service.StatisticInfoService;
import java.util.List;
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
    List<AddressBalanceRanking> list = baseMapper.selectObjs(queryWrapper);
    return list;
  }
}