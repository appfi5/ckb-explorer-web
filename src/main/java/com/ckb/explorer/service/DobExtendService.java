package com.ckb.explorer.service;

import com.ckb.explorer.domain.resp.NftCollectionResponse;
import com.ckb.explorer.domain.resp.NftResponse;
import java.util.List;

public interface DobExtendService {

  NftResponse getNftByTokenId(String tokenId);

  List<NftCollectionResponse> getNftCollectionsByName(String name);

  List<NftCollectionResponse> getNftCollectionsByClusterId(String clusterId);

  NftCollectionResponse getNftCollectionsByClusterTypeHash(String clusterTypeHash);
}
