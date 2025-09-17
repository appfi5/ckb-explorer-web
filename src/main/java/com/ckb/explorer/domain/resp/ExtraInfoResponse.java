package com.ckb.explorer.domain.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.math.BigInteger;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExtraInfoResponse {

  private String type;

  @JsonSerialize(using = ToStringSerializer.class)
  private BigInteger capacity;

  // udt xudt xudt_compatible ssri
  private String symbol;

  @JsonSerialize(using = ToStringSerializer.class)
  private BigInteger amount;

  @JsonSerialize(using = ToStringSerializer.class)
  private Integer decimal;

  private String typeHash;

  private Boolean published;

  // cota_registry
  private String codeHash;
  // cota_regular

  // m_nft_issuer m_nft_class m_nft_token
  private String issuerName;

  private String className;

  private String total;

  private String tokenId;

  private List<String> collection;

    // nrc_721_token nrc_721_factory


  // spore_cluster spore_cell did_cell
  private String clusterName;

  // omiga_inscription_info omiga_inscription
  private String name;

  //"stablepp_pool"

}
