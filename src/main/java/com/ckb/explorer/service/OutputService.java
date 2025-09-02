package com.ckb.explorer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ckb.explorer.domain.resp.AddressLiveCellsResponse;
import com.ckb.explorer.entity.Output;

public interface OutputService extends IService<Output> {

  Page<AddressLiveCellsResponse> getAddressLiveCellsByAddress(String address, String tag, String sort, Boolean boundStatus, int page, int pageSize);

  Long countAddressTransactions(Long scriptId);
}