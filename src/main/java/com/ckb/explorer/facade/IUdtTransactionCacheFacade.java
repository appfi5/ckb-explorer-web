package com.ckb.explorer.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.resp.AddressTransactionPageResponse;
import com.ckb.explorer.domain.resp.UdtTransactionPageResponse;

/**
 * IUdtTransactionCacheFacade 提供UDT交易相关的缓存操作接口
 */
public interface IUdtTransactionCacheFacade {

    /**
     * 根据地址获取交易列表（分页）
     *
     * @param typeScriptHash udt typeScript哈希
     * @param sort 排序方式
     * @param page 页码
     * @param pageSize 每页大小
     * @return 交易列表响应
     */
    Page<UdtTransactionPageResponse> getUdtTransactions(String typeScriptHash, String sort, Integer page, Integer pageSize);
}