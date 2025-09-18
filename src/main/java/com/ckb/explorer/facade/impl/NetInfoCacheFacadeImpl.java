package com.ckb.explorer.facade.impl;

import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.resp.NetInfoResponse;
import com.ckb.explorer.facade.INetInfoCacheFacade;
import com.ckb.explorer.util.CacheUtils;
import jakarta.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.nervos.ckb.CkbRpcApi;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class NetInfoCacheFacadeImpl implements INetInfoCacheFacade {

  @Resource
  private CacheUtils cacheUtils;

  @Resource
  private CkbRpcApi ckbRpcApi;

  // 缓存 TTL
  private static final long TTL_SECONDS = 4 * 60 * 60;

  private static final String CACHE_PREFIX = "node:info:version";
  private static final String CACHE_VERSION = "v1";
  @Override
  public NetInfoResponse getLocalNodeInfoVersion() {
    String cacheKey = String.format("%s%s", CACHE_PREFIX, CACHE_VERSION);

    return cacheUtils.getCache(
        cacheKey,                    // 缓存键
        this::loadNodeInfoVersion,  // 数据加载函数
        TTL_SECONDS,                 // 缓存过期时间
        TimeUnit.SECONDS             // 时间单位
    );
  }

  private NetInfoResponse loadNodeInfoVersion() {
    NetInfoResponse response = new NetInfoResponse();
    try {
      var nodeInfo = ckbRpcApi.localNodeInfo();
      response.setVersion(nodeInfo.version);
    } catch (IOException e) {
      throw new ServerException(I18nKey.LOCAL_NODE_INFO_NOT_FOUND_CODE, I18nKey.LOCAL_NODE_INFO_NOT_FOUND_MESSAGE);
    }

    return response;
  }
}
