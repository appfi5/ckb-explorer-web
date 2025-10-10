package com.ckb.explorer.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.req.ContractTransactionsPageReq;
import com.ckb.explorer.domain.resp.ContractTransactionPageResponse;

/**
 * IContractTransactionCacheFacade 提供合约交易相关的缓存操作接口
 */
public interface IContractTransactionCacheFacade {

    /**
     * 根据合约ID获取交易列表（分页）
     *
     * @param req 分页请求参数
     * @return 交易列表响应
     */
    Page<ContractTransactionPageResponse> getContractTransactions(ContractTransactionsPageReq req);
}