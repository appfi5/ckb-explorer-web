package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ckb.explorer.entity.StatisticAddress;
import org.apache.ibatis.annotations.Mapper;

/**
 * StatisticAddressMapper 地址统计信息Mapper接口
 * 用于操作statistic_address表
 */
@Mapper
public interface StatisticAddressMapper extends BaseMapper<StatisticAddress> {

}