package com.ckb.explorer.domain.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NftCollectionResponse {

  // 11-type clusterId,clusterName,typeScriptHash
  private String type = "nft-collection";
  private Long id;
  private String collectionId;
  private String collectionName;
  private String typeScriptHash;
}
