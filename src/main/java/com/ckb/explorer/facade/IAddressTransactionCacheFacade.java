package com.ckb.explorer.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.req.AddressTransactionsPageReq;
import com.ckb.explorer.domain.resp.AddressTransactionPageResponse;

/**
 * IAddressTransactionCacheFacade 提供地址交易相关的缓存操作接口
 */
public interface IAddressTransactionCacheFacade {

    /**
     * 根据地址获取交易列表（分页）
     *
     * @param address 地址哈希
     * @param req
     * @return 交易列表响应
     */
    Page<AddressTransactionPageResponse> getAddressTransactions(String address, AddressTransactionsPageReq req);
}