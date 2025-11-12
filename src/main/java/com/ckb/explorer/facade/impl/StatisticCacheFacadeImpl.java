package com.ckb.explorer.facade.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ckb.explorer.domain.resp.EpochInfoResponse;
import com.ckb.explorer.domain.resp.IndexStatisticResponse;
import com.ckb.explorer.domain.resp.StatisticResponse;
import com.ckb.explorer.entity.Block;
import com.ckb.explorer.facade.IStatisticCacheFacade;
import com.ckb.explorer.mapper.BlockMapper;
import com.ckb.explorer.mapstruct.BlockchainInfoConvert;
import com.ckb.explorer.service.StatisticInfoService;
import com.ckb.explorer.util.CacheUtils;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.nervos.ckb.type.BlockchainInfo;
import org.nervos.ckb.utils.Numeric;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StatisticCacheFacadeImpl implements IStatisticCacheFacade {

  @Resource
  private CacheUtils cacheUtils;

  @Resource
  private BlockMapper blockMapper;

  @Resource
  private StatisticInfoService statisticInfoService;


  private static final String STATISTIC_CACHE_PREFIX = "statistic:";
  private static final String CACHE_VERSION = "v1";

  // 缓存 TTL
  private static final long TTL_SECONDS = 15;

  @Override
  public IndexStatisticResponse getIndexStatistic() {
    // 创建缓存键
    String cacheKey = String.format("%s%s", STATISTIC_CACHE_PREFIX, CACHE_VERSION);

    return cacheUtils.getCache(
        cacheKey,                    // 缓存键
        this::loadFromDatabase,  // 数据加载函数
        TTL_SECONDS,                 // 缓存过期时间
        TimeUnit.SECONDS             // 时间单位
    );
  }


  private IndexStatisticResponse loadFromDatabase() {
    IndexStatisticResponse response = new IndexStatisticResponse();
    LambdaQueryWrapper<Block> wrapper = new LambdaQueryWrapper<Block>().orderByDesc(Block::getId)
        .last("LIMIT 1");

    var tipBlock = blockMapper.selectOne(wrapper);

    var tipBlockNumber = tipBlock.getBlockNumber();
    // epochInfo
    EpochInfoResponse epochInfo = new EpochInfoResponse();
    epochInfo.setEpochNumber(tipBlock.getEpochNumber());
    epochInfo.setEpochLength(tipBlock.getEpochLength());
    epochInfo.setIndex(tipBlock.getBlockNumber() - tipBlock.getStartNumber());
    response.setEpochInfo(epochInfo);

    response.setTipBlockNumber(tipBlockNumber);
    var currentEpochDifficulty = Numeric.toBigInt(tipBlock.getDifficulty());
    response.setCurrentEpochDifficulty(currentEpochDifficulty);

    var statisticInfo = statisticInfoService.getStatisticInfo();
    if (statisticInfo == null) {
      return response;
    }
    response.setAverageBlockTime(statisticInfo.getAverageBlockTime()); // 保留两位小数
    var hashRate = statisticInfo.getHashRate();
    response.setHashRate(hashRate);
    BigDecimal estimatedEpochTime = new BigDecimal(currentEpochDifficulty)
            .multiply(new BigDecimal(tipBlock.getEpochLength()))
        .divide(hashRate,6, RoundingMode.HALF_UP);
    response.setEstimatedEpochTime(estimatedEpochTime);
    response.setTransactionsLast24hrs(statisticInfo.getTransactionsLast24hrs());
    response.setTransactionsCountPerMinute(statisticInfo.getTransactionsCountPerMinute());
    return response;
  }


  @Override
  public StatisticResponse getStatisticByFieldName(String fieldName) {
    // 创建缓存键
    String cacheKey = String.format("%s%s:fieldName:%s", STATISTIC_CACHE_PREFIX, CACHE_VERSION,
        fieldName);
    return cacheUtils.getCache(
        cacheKey,                    // 缓存键
        () -> loadFromDatabase(fieldName),  // 数据加载函数
        TTL_SECONDS,                 // 缓存过期时间
        TimeUnit.SECONDS             // 时间单位
    );
  }

  private StatisticResponse loadFromDatabase(String fieldName) {
    StatisticResponse response = new StatisticResponse();

    // 获取最新区块信息
    LambdaQueryWrapper<Block> wrapper = new LambdaQueryWrapper<Block>().orderByDesc(Block::getId)
        .last("LIMIT 1");

    Block tipBlock = blockMapper.selectOne(wrapper);

    // 根据fieldName设置对应的统计信息
    if ("tip_block_number".equals(fieldName)) {
      response.setTipBlockNumber(tipBlock.getBlockNumber());
    } else if ("blockchain_info".equals(fieldName)) {
      BlockchainInfo data = statisticInfoService.getBlockchainInfo();
      response.setBlockchainInfo(data == null? null : BlockchainInfoConvert.INSTANCE.toConvert(data));
    } else if ("address_balance_ranking".equals(fieldName)) {
      response.setAddressBalanceRanking(statisticInfoService.getAddressBalanceRanking());
    } else if ("transaction_fees".equals(fieldName)) {
      response.setTransactionFeeRates(statisticInfoService.getTransactionFeeRates());
    }

    // 设置创建时间戳
    response.setCreatedAtUnixtimestamp(tipBlock.getTimestamp());

    return response;
  }
}
