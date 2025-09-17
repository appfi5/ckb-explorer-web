package com.ckb.explorer.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.req.UdtTransactionsPageReq;
import com.ckb.explorer.domain.resp.AddressTransactionPageResponse;
import com.ckb.explorer.domain.resp.UdtTransactionPageResponse;

/**
 * IUdtTransactionCacheFacade 提供UDT交易相关的缓存操作接口
 */
public interface IUdtTransactionCacheFacade {

    /**
     * 根据type Script 哈希获取交易列表（分页）
     *
     * @param typeScriptHash udt typeScript哈希
     * @param pageReq 分页查询参数
     * @return 交易列表响应
     */
    Page<UdtTransactionPageResponse> getUdtTransactions(String typeScriptHash, UdtTransactionsPageReq pageReq);
}