package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ckb.explorer.domain.resp.MinerDailyStatisticsResponse;
import com.ckb.explorer.domain.resp.MinerDailyStatisticsStartEndTimeResponse;
import com.ckb.explorer.entity.MinerDailyStatistics;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MinerDailyStatisticsMapper extends BaseMapper<MinerDailyStatistics> {

  @Select("select min(created_at_unixtimestamp) AS start_time, max(created_at_unixtimestamp) AS end_time from miner_daily_statistics")
  MinerDailyStatisticsStartEndTimeResponse getStartEndTime();

  @Select("select * from miner_daily_statistics where created_at_unixtimestamp = #{date}")
  MinerDailyStatistics getByDate(Long  date);

  @Select("select id, avg_ror, created_at_unixtimestamp from miner_daily_statistics order by id desc limit 30")
  List<MinerDailyStatisticsResponse> getAvgRor();
}
