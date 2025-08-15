package com.ckb.explorer.facade;

import com.ckb.explorer.common.dto.ResponsePageInfo;
import com.ckb.explorer.domain.resp.BlockListResponse;
import com.ckb.explorer.domain.resp.base.BaseResponse;
import java.util.List;

public interface IBlockCacheFacade {

  ResponsePageInfo<List<BaseResponse<BlockListResponse>>> getBlocksByPage(int pageNum, int pageSize, String sort);
}
