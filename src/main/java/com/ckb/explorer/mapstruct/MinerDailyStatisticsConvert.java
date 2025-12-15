package com.ckb.explorer.mapstruct;

import com.ckb.explorer.domain.resp.MinerDailyStatisticsResponse;
import com.ckb.explorer.entity.MinerDailyStatistics;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MinerDailyStatisticsConvert {
  MinerDailyStatisticsConvert INSTANCE = Mappers.getMapper(MinerDailyStatisticsConvert.class);

  @Mapping(target = "miners",expression = "java(statistics.getMiners()!= null?statistics.getMiners().getData(): java.util.Collections.emptyList())")
  MinerDailyStatisticsResponse toConvert(MinerDailyStatistics statistics);
}
