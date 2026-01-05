package com.ckb.explorer.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.resp.PendingTransactionPageResponse;

public interface ICkbPendingTransactionCacheFacade {
  /**
   * 获取交易列表（分页）
   *
   * @param page 页码
   * @param size 每页大小
   * @return 交易列表响应
   */
  Page<PendingTransactionPageResponse> getPendingTransactionsByPage(Integer page, Integer size);
}
