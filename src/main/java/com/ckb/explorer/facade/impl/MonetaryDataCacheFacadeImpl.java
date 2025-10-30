package com.ckb.explorer.facade.impl;

import com.ckb.explorer.domain.resp.MonetaryDataResponse;
import com.ckb.explorer.facade.IMonetaryDataCacheFacade;
import com.ckb.explorer.util.CacheUtils;
import com.ckb.explorer.util.MonetaryData;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MonetaryDataCacheFacadeImpl implements IMonetaryDataCacheFacade {

  @Resource
  private CacheUtils cacheUtils;

  private static final String CACHE_PREFIX = "monetary_data:";
  private static final String CACHE_VERSION = "v1";
  // 缓存 TTL: 1小时
  private static final long TTL_SECONDS = 60 * 60;
  @Override
  public MonetaryDataResponse getMonetaryData(String indicator) {

    // 创建缓存键，包含limit和indicator参数
    String cacheKey = String.format("%s%s:indicator:%s",
        CACHE_PREFIX, CACHE_VERSION,
        indicator);

    return cacheUtils.getCache(
        cacheKey,                    // 缓存键
        () -> loadData(indicator),  // 数据加载函数
        TTL_SECONDS,                 // 缓存过期时间
        TimeUnit.SECONDS             // 时间单位
    );
  }

  private MonetaryDataResponse loadData(String indicator) {
    MonetaryDataResponse result = new MonetaryDataResponse();
    List<String> queryKeys = Arrays.stream(indicator.split("-"))
        .map(String::trim) // 去除每个key的前后空格（避免"nominal_apc 50"这类无效格式）
        .filter(key -> !key.isEmpty()) // 过滤分割后的空字符串（如queryKey以"-"开头/结尾）
        .toList();
    for (String queryKey : queryKeys) {
      if("nominal_apc".equals(queryKey)){
        var nominalApc = MonetaryData.nominalApc();
        result.setNominalApc(nominalApc.stream().map(apc -> apc.toPlainString()).toList());
      }
      if("nominal_inflation_rate".equals(queryKey)){
        var nominalInflationRate = MonetaryData.nominalInflationRate();
        result.setNominalInflationRate(nominalInflationRate.stream().map(rate -> rate.toPlainString()).toList());
      }
      if("real_inflation_rate".equals(queryKey)){
        var realInflationRate = MonetaryData.realInflationRate();
        result.setRealInflationRate(realInflationRate.stream().map(rate -> rate.toPlainString()).toList());
      }
      if(queryKey.matches(MonetaryData.NOMINAL_APC_REGEX)){
        int maxYear = Integer.parseInt(queryKey.replace("nominal_apc", ""));
        var nominalApc = MonetaryData.nominalApc(maxYear);
        result.setNominalApc(nominalApc.stream().map( apc -> apc.toPlainString()).toList());
      }
    }
    return  result;
  }

}
