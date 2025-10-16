package com.ckb.explorer.domain.resp;

import lombok.Data;

@Data
public class NftResponse {
  private String type = "nft";
  private String tokenId;
  private String collectionId;
  private String collectionName;
  private String typeScriptHash;

}
