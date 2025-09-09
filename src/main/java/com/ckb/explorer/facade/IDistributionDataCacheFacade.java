package com.ckb.explorer.facade;

import com.ckb.explorer.domain.resp.DistributionDataResponse;

/**
 * IDistributionDataCacheFacade 提供分布数据相关的缓存操作接口
 */
public interface IDistributionDataCacheFacade {

    /**
     * 根据指标名称获取分布数据
     * @param indicator 指标名称
     * @return 分布数据
     */
    DistributionDataResponse getDistributionDataByIndicator(String indicator);
}