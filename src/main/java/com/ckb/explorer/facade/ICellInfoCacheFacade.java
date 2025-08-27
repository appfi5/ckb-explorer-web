package com.ckb.explorer.facade;

import com.ckb.explorer.domain.resp.CellInfoResponse;


public interface ICellInfoCacheFacade {

  CellInfoResponse findByOutputId(String id);
}
