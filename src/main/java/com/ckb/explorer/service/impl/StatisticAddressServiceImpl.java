package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.entity.StatisticAddress;
import com.ckb.explorer.mapper.StatisticAddressMapper;
import com.ckb.explorer.service.StatisticAddressService;
import org.springframework.stereotype.Service;

/**
 * StatisticAddressServiceImpl 地址统计信息服务实现类
 */
@Service
public class StatisticAddressServiceImpl extends ServiceImpl<StatisticAddressMapper, StatisticAddress> implements StatisticAddressService {

}