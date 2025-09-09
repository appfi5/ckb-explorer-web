package com.ckb.explorer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ckb.explorer.entity.DailyStatistics;

/**
 * DailyStatisticsService 提供每日统计数据相关的业务操作接口
 */
public interface DailyStatisticsService extends IService<DailyStatistics> {

    /**
     * 获取有效的每日统计数据，并按创建时间戳升序排序
     * @return 有效的每日统计数据列表
     */
    //List<DailyStatistics> getValidIndicatorsOrderByCreatedAtAsc();

    /**
     * 根据指标名称获取统计数据
     * @param indicator 指标名称
     * @return 统计数据列表
     */
    Object getByIndicator(String indicator);
}