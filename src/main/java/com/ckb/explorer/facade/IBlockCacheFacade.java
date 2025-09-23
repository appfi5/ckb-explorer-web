package com.ckb.explorer.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.resp.BlockListResponse;
import com.ckb.explorer.domain.resp.BlockResponse;
import java.util.List;


public interface IBlockCacheFacade {

  List<BlockListResponse> getHomePageBlocks(int size);

  Page<BlockListResponse> getBlocksByPage(int pageNum, int pageSize, String sort);

  BlockResponse findBlock(String id);
}
