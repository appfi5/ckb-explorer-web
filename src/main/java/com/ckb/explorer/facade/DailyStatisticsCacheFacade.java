package com.ckb.explorer.facade;

import com.ckb.explorer.domain.resp.DailyStatisticResponse;
import java.util.List;

/**
 * DailyStatisticsCacheFacade 提供每日统计数据相关的缓存操作接口
 */
public interface DailyStatisticsCacheFacade {

    /**
     * 根据指标名称获取每日统计数据
     * @param indicator 指标名称
     * @param limit 获取的条数
     * @return 统计数据列表
     */
    List<DailyStatisticResponse> getDailyStatisticsByIndicator(String indicator, Integer limit);
}