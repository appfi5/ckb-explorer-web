package com.ckb.explorer.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.resp.BlockTransactionPageResponse;

/**
 * IBlockTransactionCacheFacade 提供块内交易相关的缓存操作接口
 */
public interface IBlockTransactionCacheFacade {

    /**
     * 根据区块哈希或区块号获取区块内的交易列表（分页）
     *
     * @param blockId 区块哈希或区块号
     * @param txHash 交易哈希（可选）
     * @param addressHash 地址哈希（可选）
     * @param page 页码
     * @param pageSize 每页大小
     * @return 交易列表响应
     */
    Page<BlockTransactionPageResponse> getBlockTransactions(String blockId, String txHash, String addressHash, Integer page, Integer pageSize);
}