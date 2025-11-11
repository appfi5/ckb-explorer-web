package com.ckb.explorer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ckb.explorer.domain.resp.CellInfoResponse;
import com.ckb.explorer.domain.resp.CellOutputDataResponse;
import com.ckb.explorer.entity.Output;

public interface OutputService extends IService<Output> {

  Long countAddressTransactions(Long scriptId);

  CellInfoResponse getCellInfo(Long id);

  CellOutputDataResponse getData(Long id);
}