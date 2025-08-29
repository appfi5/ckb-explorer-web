package com.ckb.explorer.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.resp.AddressLiveCellsResponse;

public interface ICellOutputCacheFacade {

  Page<AddressLiveCellsResponse> getAddressLiveCellsByAddress(String address, String tag, String sort, Boolean boundStatus, int page, int pageSize);
}
