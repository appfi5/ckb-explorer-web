package com.ckb.explorer.service;

import com.ckb.explorer.entity.RollingAvgBlockTime;
import java.util.List;

/**
 * AverageBlockTimeService 平均区块时间服务接口
 * 定义平均区块时间相关的服务方法
 */
public interface AverageBlockTimeService {

    /**
     * 获取所有滚动平均区块时间数据
     * @param limit 查询限制
     * @return 滚动平均区块时间数据
     */
    List<RollingAvgBlockTime> getAvgBlockTime(Integer limit);
}