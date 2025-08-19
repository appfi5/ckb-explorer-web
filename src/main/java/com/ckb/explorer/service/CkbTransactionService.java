package com.ckb.explorer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ckb.explorer.entity.CkbTransaction;

public interface CkbTransactionService extends IService<CkbTransaction> {

  /**
   * 分页查询交易列表
   * @param pageNum 当前页码
   * @param pageSize 每页条数
   * @param sort 排序字段和方式，格式为"字段名.排序方式"，如"number.desc"
   * @return 分页结果
   */
  Page<CkbTransaction> getCkbTransactionsByPage(int pageNum, int pageSize, String sort);
}