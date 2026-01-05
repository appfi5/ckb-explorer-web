package com.ckb.explorer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.resp.CellInputResponse;
import com.ckb.explorer.domain.resp.CellOutputResponse;
import com.ckb.explorer.domain.resp.PendingTransactionPageResponse;
import com.ckb.explorer.domain.resp.TransactionResponse;

public interface CkbPendingTransactionService {
  /**
   * 分页查询pending交易列表
   * @param pageNum 当前页码
   * @param pageSize 每页条数
   * @return 分页结果
   */
  Page<PendingTransactionPageResponse> getCkbPendingTransactionsByPage(int pageNum, int pageSize);

  /**
   * 根据交易哈希获取pending交易详情
   *
   * @param txHash 交易哈希
   * @return 交易详情响应
   */
  TransactionResponse getPendingTransactionByHash(String txHash);

  /**
   * 获取交易的显示输入
   *
   * @param txHash 交易哈希
   * @param page 页码
   * @param pageSize 每页大小
   * @return 分页的输入单元格列表
   */
  Page<CellInputResponse> getDisplayInputs(String txHash, int page, int pageSize);

  /**
   * 获取交易的显示输出
   *
   * @param txHash 交易哈希
   * @param page 页码
   * @param pageSize 每页大小
   * @return 分页的输出单元格列表
   */
  Page<CellOutputResponse> getDisplayOutputs(String txHash, int page, int pageSize);
}
