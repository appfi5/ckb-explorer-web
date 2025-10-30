package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ckb.explorer.entity.UdtDailyStatistics;
import com.ckb.explorer.domain.resp.UdtDailyStatisticsResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UdtDailyStatisticsMapper extends BaseMapper<UdtDailyStatistics> {


  @Select("select created_at_unixtimestamp, SUM(ckb_transactions_count) AS ckb_transactions_count, SUM(holders_count) AS holders_count from udt_daily_statistics group by created_at_unixtimestamp order by created_at_unixtimestamp desc")
  List<UdtDailyStatisticsResponse> getUdtDailyStatistics();
}
