package com.ckb.explorer.domain.resp;

import lombok.Data;

@Data
public class EpochInfoResponse {

  public Long epochNumber;

  public Integer epochLength;

  public Long index;
}
