package com.ckb.explorer.service;

import com.ckb.explorer.domain.resp.EpochStatisticsResponse;
import java.util.List;

/**
 * EpochStatisticsService 提供纪元统计数据的服务接口
 */
public interface EpochStatisticsService {

    /**
     * 获取纪元统计数据列表
     * @param limit 限制数量（可选）
     * @param indicator 指标名称
     * @return 纪元统计数据列表
     */
    List<EpochStatisticsResponse> getEpochStatistics(Integer limit, String indicator);
}