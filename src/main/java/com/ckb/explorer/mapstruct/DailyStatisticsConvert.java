package com.ckb.explorer.mapstruct;

import com.ckb.explorer.domain.resp.DailyStatisticResponse;
import com.ckb.explorer.entity.DailyStatistics;
import com.ckb.explorer.util.TypeConversionUtil;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {TypeConversionUtil.class})
public interface DailyStatisticsConvert {
  DailyStatisticsConvert INSTANCE = Mappers.getMapper(DailyStatisticsConvert.class);

  @Named("toConvertWithBurnt")
  @Mapping(target = "burnt",expression = "java(dailyStatistics.getTreasuryAmount()!=null && isBurnt? new java.math.BigDecimal(dailyStatistics.getTreasuryAmount()).add(new java.math.BigDecimal(\"84\").multiply(new java.math.BigDecimal(\"10\").pow(16))): null)")
  @Mapping(target = "liquidity",expression = "java(dailyStatistics.getCirculatingSupply()!=null && dailyStatistics.getTotalDaoDeposit()!= null? dailyStatistics.getCirculatingSupply().subtract(new java.math.BigDecimal(dailyStatistics.getTotalDaoDeposit())): null)")
  @Mapping(target = "treasuryAmount",expression = "java(!isBurnt? dailyStatistics.getTreasuryAmount(): null)")
  DailyStatisticResponse toConvert(DailyStatistics dailyStatistics, boolean isBurnt);


  /**
   * 集合转换，指定使用的元素转换方法和返回的具体集合类型
   */
  @IterableMapping(qualifiedByName = "toConvertWithBurnt", elementTargetType = DailyStatisticResponse.class)
  default List<DailyStatisticResponse> toConvertList(List<DailyStatistics> dailyStatisticsList, boolean isBurnt) {
    // 提供默认实现，明确使用ArrayList作为具体实现
    if (dailyStatisticsList == null) {
      return null;
    }
    List<DailyStatisticResponse> result = new ArrayList<>(dailyStatisticsList.size());
    for (DailyStatistics dailyStatistics : dailyStatisticsList) {
      result.add(toConvert(dailyStatistics, isBurnt));
    }
    return result;
  }
}
