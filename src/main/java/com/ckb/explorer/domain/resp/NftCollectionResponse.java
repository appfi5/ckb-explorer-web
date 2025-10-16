package com.ckb.explorer.domain.resp;

import lombok.Data;

@Data
public class NftCollectionResponse {

  // 11-type clusterId,clusterName,typeScriptHash
  private String type = "nft-collection";
  private Long id;
  private String collectionId;
  private String collectionName;
  private String typeScriptHash;
}
