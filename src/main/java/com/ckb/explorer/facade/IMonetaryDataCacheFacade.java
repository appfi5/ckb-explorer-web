package com.ckb.explorer.facade;

import com.ckb.explorer.domain.resp.MonetaryDataResponse;

public interface IMonetaryDataCacheFacade {
  MonetaryDataResponse getMonetaryData(String indicator);
}
