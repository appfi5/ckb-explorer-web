package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ckb.explorer.entity.DailyStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author dell
* @description 针对表【daily_statistics】的数据库操作Mapper
* @createDate 2025-08-18 17:48:35
* @Entity com.ckb.explore.worker.entity.DailyStatistics
*/
@Mapper
public interface DailyStatisticsMapper extends BaseMapper<DailyStatistics> {

  @Select("select * from daily_statistics order by created_at_unixtimestamp desc limit 1")
  DailyStatistics getLastDayDailyStatistics();
}




