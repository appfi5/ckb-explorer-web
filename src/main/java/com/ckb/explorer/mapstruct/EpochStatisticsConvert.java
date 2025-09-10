package com.ckb.explorer.mapstruct;

import com.ckb.explorer.domain.resp.EpochStatisticsResponse;
import com.ckb.explorer.domain.resp.LargestBlockResponse;
import com.ckb.explorer.domain.resp.LargestTxResponse;
import com.ckb.explorer.entity.EpochStatistics;
import com.ckb.explorer.util.TypeConversionUtil;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {TypeConversionUtil.class})
public interface EpochStatisticsConvert {
  EpochStatisticsConvert INSTANCE = Mappers.getMapper(EpochStatisticsConvert.class);

  @Mapping(target = "largestBlock", ignore = true)
  @Mapping(target = "largestTx", ignore = true)
  EpochStatisticsResponse toConvert(EpochStatistics epochStatistics);

  List<EpochStatisticsResponse> toConvertList(List<EpochStatistics> epochStatisticsList);

  @AfterMapping
  default void setAdditionalFields(@MappingTarget EpochStatisticsResponse response, EpochStatistics epochStatistics) {

      response.setLargestBlock(new LargestBlockResponse(epochStatistics.getLargestBlockNumber(), epochStatistics.getLargestBlockSize()));
      response.setLargestTx(new LargestTxResponse(TypeConversionUtil.byteToStringHash(epochStatistics.getLargestTxHash()), epochStatistics.getLargestTxBytes()));

  }
}
