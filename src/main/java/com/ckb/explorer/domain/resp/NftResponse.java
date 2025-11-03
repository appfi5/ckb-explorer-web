package com.ckb.explorer.domain.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NftResponse {
  private String type = "nft";
  private String tokenId;
  private String collectionId;
  private String collectionName;
  private String typeScriptHash;

}
