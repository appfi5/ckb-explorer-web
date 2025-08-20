package com.ckb.explorer.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.resp.TransactionPageResponse;

/**
 * ITransactionCacheFacade 提供交易相关的缓存操作接口
 */
public interface ICkbTransactionCacheFacade {

  /**
   * 获取交易列表（分页）
   *
   * @param page 页码
   * @param size 每页大小
   * @param sort 排序规则
   * @return 交易列表响应
   */
  Page<TransactionPageResponse> getTransactionsByPage(Integer page, Integer size, String sort);

  /**
   * 根据交易哈希获取交易详情
   *
   * @param txHash 交易哈希
   * @return 交易详情响应
   */
  TransactionPageResponse getTransactionByHash(String txHash);
}