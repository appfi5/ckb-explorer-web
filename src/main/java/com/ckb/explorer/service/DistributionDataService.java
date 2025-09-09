package com.ckb.explorer.service;

import com.ckb.explorer.domain.resp.DistributionDataResponse;

/**
 * DistributionDataService 提供分布数据相关的业务逻辑接口
 */
public interface DistributionDataService {

    /**
     * 获取平均区块时间数据
     * @return 平均区块时间数据
     */
    DistributionDataResponse getAverageBlockTime();

    /**
     * 根据指标名称获取分布数据
     * @param indicator 指标名称
     * @return 分布数据
     */
    DistributionDataResponse getDistributionDataByIndicator(String indicator);
}