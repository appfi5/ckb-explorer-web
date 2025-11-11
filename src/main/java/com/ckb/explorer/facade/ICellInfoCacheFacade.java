package com.ckb.explorer.facade;

import com.ckb.explorer.domain.resp.CellInfoResponse;
import com.ckb.explorer.domain.resp.CellOutputDataResponse;


public interface ICellInfoCacheFacade {

  CellInfoResponse findByOutputId(String id);

  CellOutputDataResponse getOutputDataById(String id);
}
