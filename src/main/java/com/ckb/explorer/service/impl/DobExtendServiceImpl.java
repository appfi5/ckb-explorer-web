package com.ckb.explorer.service.impl;

import com.ckb.explorer.domain.resp.NftCollectionResponse;
import com.ckb.explorer.domain.resp.NftResponse;
import com.ckb.explorer.mapper.DobExtendMapper;
import com.ckb.explorer.service.DobExtendService;
import jakarta.annotation.Resource;
import java.util.List;
import org.nervos.ckb.utils.Numeric;
import org.springframework.stereotype.Service;

@Service
public class DobExtendServiceImpl implements DobExtendService {

  @Resource
  private DobExtendMapper dobExtendMapper;
  @Override
  public NftResponse getNftByTokenId(String tokenId) {
    return dobExtendMapper.getNftByTokenId(Numeric.hexStringToByteArray(tokenId));
  }

  @Override
  public List<NftCollectionResponse> getNftCollectionsByName(String name) {
    return dobExtendMapper.getNftCollectionsByName("%"+name.toLowerCase()+"%");
  }

  @Override
  public List<NftCollectionResponse> getNftCollectionsByClusterId(String clusterId) {
    return dobExtendMapper.getNftCollectionsByClusterId(Numeric.hexStringToByteArray(clusterId));
  }

  @Override
  public NftCollectionResponse getNftCollectionsByClusterTypeHash(String clusterTypeHash) {
    return dobExtendMapper.getNftCollectionsByClusterTypeHash(Numeric.hexStringToByteArray(clusterTypeHash));
  }
}
