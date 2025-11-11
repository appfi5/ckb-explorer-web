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
public class AccountUdtBalanceResponse {
  private String fullName;

  private String symbol;

  private Integer decimal;

  @JsonSerialize(using = ToStringSerializer.class)
  private BigInteger amount;

  private String udtIconFile;

  private Integer udtType;

  private ScriptResponse udtTypeScript;

  private Long typeScriptId;

  private String typeScriptHash;

  @JsonSerialize(using = ToStringSerializer.class)
  private Long totalAmount;

  private String expectedSupply; // omiga相关

  private String mint_status; // omiga相关 minting: 0, closed: 1, rebase_start: 2

  private List<CollectionTypeHashResponse> collection;// m_nft_token,nrc_721_token, "spore_cell", "did_cell"
}
