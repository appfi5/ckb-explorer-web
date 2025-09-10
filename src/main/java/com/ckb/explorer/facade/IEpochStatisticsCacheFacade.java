package com.ckb.explorer.facade;

import com.ckb.explorer.domain.resp.EpochStatisticsResponse;
import java.util.List;

/**
 * IEpochStatisticsCacheFacade 提供纪元统计数据相关的缓存操作接口
 */
public interface IEpochStatisticsCacheFacade {

    /**
     * 获取纪元统计数据列表
     * @param limit 限制数量（可选）
     * @param indicator 指标名称
     * @return 纪元统计数据列表
     */
    List<EpochStatisticsResponse> getEpochStatistics(Integer limit, String indicator);
}