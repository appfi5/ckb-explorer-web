package com.ckb.explorer.mapstruct;

import com.ckb.explorer.domain.resp.DailyStatisticResponse;
import com.ckb.explorer.entity.DailyStatistics;
import com.ckb.explorer.util.TypeConversionUtil;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {TypeConversionUtil.class})
public interface DailyStatisticsConvert {
  DailyStatisticsConvert INSTANCE = Mappers.getMapper(DailyStatisticsConvert.class);

  DailyStatisticResponse toConvert(DailyStatistics dailyStatistics);

  List<DailyStatisticResponse> toConvertList(List<DailyStatistics> dailyStatisticsList);
}
