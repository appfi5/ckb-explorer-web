package com.ckb.explorer.domain.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigInteger;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nervos.ckb.type.AlertMessage;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlockchainInfoResponse {

  public boolean isInitialBlockDownload;
  public long epoch;
  public BigInteger difficulty;
  public long medianTime;
  public String chain;
  public List<AlertMessage> alerts;
}
