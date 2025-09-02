package com.ckb.explorer.facade.impl;

import com.ckb.explorer.domain.resp.CellInfoResponse;
import com.ckb.explorer.entity.Output;
import com.ckb.explorer.entity.Script;
import com.ckb.explorer.facade.ICellInfoCacheFacade;
import com.ckb.explorer.mapstruct.LockScriptConvert;
import com.ckb.explorer.mapstruct.TypeScriptConvert;
import com.ckb.explorer.service.OutputService;
import com.ckb.explorer.service.ScriptService;
import com.ckb.explorer.util.TypeConversionUtil;
import jakarta.annotation.Resource;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.nervos.ckb.utils.Numeric;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@Transactional(readOnly = true)
public class CellInfoCacheFacadeImpl implements ICellInfoCacheFacade {

  @Resource
  private RedissonClient redissonClient;

  @Resource
  private OutputService outputService;

  @Resource
  private ScriptService scriptService;

  private static final String Cell_INFO_CACHE_PREFIX = "cellInfo:";
  private static final String CACHE_VERSION = "v1";

  // 缓存 TTL
  private static final long TTL_SECONDS = 60;
  private static final long TTL_MILLIS = TimeUnit.SECONDS.toMillis(TTL_SECONDS);
  // 防击穿锁等待时间
  private static final long LOCK_WAIT_TIME = 1;
  private static final long LOCK_LEASE_TIME = 8;

  @Override
  public CellInfoResponse findByOutputId(String id) {
    // 创建缓存键
    String cacheKey = String.format("%s%s:id", Cell_INFO_CACHE_PREFIX, CACHE_VERSION, id);

    RBucket<CellInfoResponse> bucket = redissonClient.getBucket(cacheKey);

    // 1. 先尝试读缓存
    CellInfoResponse cached = bucket.get();
    if (cached != null) {
      return cached;
    }

    // 2. 缓存未命中，使用分布式锁防止击穿
    String lockKey = cacheKey + ":lock";
    RLock lock = redissonClient.getLock(lockKey);

    try {
      // 双重检查
      cached = bucket.get();
      if (cached != null) {
        return cached;
      }

      if (lock.tryLock(LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.SECONDS)) {
        try {
          // 再次检查
          cached = bucket.get();
          if (cached != null) {
            return cached;
          }

          // 真正加载数据
          CellInfoResponse result = loadFromDatabase(id);

          // 写入缓存
          bucket.set(result, Duration.ofMillis(TTL_MILLIS));

          return result;
        } finally {
          if (lock.isHeldByCurrentThread()) {
            lock.unlock();
          }
        }
      } else {
        // 获取锁失败，降级：直接查库
        CellInfoResponse result = loadFromDatabase(id);
        bucket.set(result, Duration.ofMillis(TTL_MILLIS));
        return result;
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      // 降级处理
      return loadFromDatabase(id);
    }
  }

  private CellInfoResponse loadFromDatabase(String id){

    CellInfoResponse response = new CellInfoResponse();
    // 根据ID查询CellOutput
    Output cellOutput = outputService.getById(Long.parseLong(id));
    if (cellOutput == null) {
      return null;
    }
    response.setCapacity(cellOutput.getCapacity());
    response.setOccupiedCapacity(cellOutput.getOccupiedCapacity());
    response.setStatus(cellOutput.getIsSpent());
    response.setCellIndex(cellOutput.getOutputIndex());
    response.setGeneratedTxHash(cellOutput.getTxHash() != null? Numeric.toHexString(cellOutput.getTxHash()) : null);
    response.setConsumedTxHash(cellOutput.getConsumedTxHash() != null && cellOutput.getConsumedTxHash().length > 0? Numeric.toHexString(cellOutput.getConsumedTxHash()) : null);

    response.setData(cellOutput.getData() != null ? Numeric.toHexString(cellOutput.getData()) : null);

    // 获取lock_script
    Script lockScript = scriptService.getById(cellOutput.getLockScriptId());
    if(lockScript == null){
      response.setLockScript(null);
    }else{
      response.setLockScript(LockScriptConvert.INSTANCE.toConvert(lockScript));
      response.setAddress(TypeConversionUtil.scriptToAddress(lockScript.getCodeHash(),
          lockScript.getArgs(),
          lockScript.getHashType()));
    }


    // 获取type_script
    Script typeScript = scriptService.getById(cellOutput.getTypeScriptId());
    if (typeScript == null) {
      response.setTypeScript(null);
    }else{
      response.setTypeScript(TypeScriptConvert.INSTANCE.toConvert(typeScript));
    }

    return response;
  }
}
