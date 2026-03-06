package com.ckb.explorer.service.impl;

import com.ckb.explorer.domain.resp.NftCollectionResponse;
import com.ckb.explorer.domain.resp.NftResponse;
import com.ckb.explorer.entity.Script;
import com.ckb.explorer.enums.NftType;
import com.ckb.explorer.mapper.DobExtendMapper;
import com.ckb.explorer.mapper.ScriptMapper;
import com.ckb.explorer.service.DobExtendService;
import com.ckb.explorer.service.ScriptService;
import jakarta.annotation.Resource;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.nervos.ckb.utils.Numeric;
import org.springframework.stereotype.Service;

@Service
public class DobExtendServiceImpl implements DobExtendService {

  @Resource
  private DobExtendMapper dobExtendMapper;

  @Resource
  ScriptService scriptService;
  @Override
  public NftResponse getNftByTokenId(String tokenId) {
    NftResponse nftResponse = dobExtendMapper.getNftByTokenId(Numeric.hexStringToByteArray(tokenId));
    if(nftResponse!=null){
      return nftResponse;
    }
    Script script =scriptService.findByScriptHash(tokenId);
    if(script==null){
      return null;
    }
    NftResponse nftByDobCodeScriptId = dobExtendMapper.getNftByDobCodeScriptId(script.getId());
    if(nftByDobCodeScriptId!=null){
      if(Objects.equals(NftType.M_NFT.getValue(),nftByDobCodeScriptId.getStandard())){
        nftByDobCodeScriptId.setTokenId(Numeric.toBigInt(Arrays.copyOfRange(script.getArgs(),24,script.getArgs().length))+"");
      }
    }
    return nftByDobCodeScriptId;
  }

  @Override
  public List<NftCollectionResponse> getNftCollectionsByName(String name) {
    name = name.replace("%", "\\%").replace("_", "\\_");
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
