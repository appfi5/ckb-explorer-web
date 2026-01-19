package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ckb.explorer.entity.RollingAvgBlockTime;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RollingAvgBlockTimeMapper extends BaseMapper<RollingAvgBlockTime> {

  /**
   * 1. 按 timestamp 升序查询所有数据（复现 Rails 的 default_scope）
   * @return 排序后的滚动平均区块时间列表
   */
  @Select({"SELECT timestamp, avg_block_time_daily, avg_block_time_weekly FROM rolling_avg_block_time ORDER BY timestamp DESC limit #{limit}"})
  List<RollingAvgBlockTime> findOrderedWithLimit(@Param("limit") Integer limit);


}
