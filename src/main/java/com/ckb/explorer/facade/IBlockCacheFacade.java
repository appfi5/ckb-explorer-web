package com.ckb.explorer.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.resp.BlockListResponse;
import com.ckb.explorer.domain.resp.BlockResponse;


public interface IBlockCacheFacade {

  Page<BlockListResponse> getBlocksByPage(int pageNum, int pageSize, String sort);

  BlockResponse findBlock(String id);
}
