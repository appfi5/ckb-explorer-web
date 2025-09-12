package com.ckb.explorer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ckb.explorer.domain.resp.LiveCellsResponse;
import com.ckb.explorer.entity.LiveCells;

/**
* @author dell
* @description 针对表【live_cells】的数据库操作Service
* @createDate 2025-09-02 13:43:49
*/
public interface LiveCellsService extends IService<LiveCells> {

  Page<LiveCellsResponse> getLiveCellsByAddressWithTypeHash(String address, String typeHash, int page, int pageSize);
}
