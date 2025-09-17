package com.ckb.explorer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.resp.UdtTransactionPageResponse;
import com.ckb.explorer.entity.UdtTransactions;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author dell
* @description 针对表【udt_transactions】的数据库操作Service
* @createDate 2025-09-09 15:20:04
*/
public interface UdtTransactionsService extends IService<UdtTransactions> {

    Page<UdtTransactionPageResponse> page(String typeScriptHash, Integer page, Integer pageSize);
}
