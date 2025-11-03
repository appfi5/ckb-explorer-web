package com.ckb.explorer.domain.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpochInfoResponse {

  public Long epochNumber;

  public Integer epochLength;

  public Long index;
}
