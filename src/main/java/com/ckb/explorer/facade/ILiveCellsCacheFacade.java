package com.ckb.explorer.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.resp.LiveCellsResponse;

public interface ILiveCellsCacheFacade {
  Page<LiveCellsResponse> getAddressLiveCellsByAddress(String address, String typeHash, int page, int pageSize);
}
