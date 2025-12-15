package com.ckb.explorer.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.req.UdtPageReq;
import com.ckb.explorer.domain.req.UdtTransactionsPageReq;
import com.ckb.explorer.domain.resp.UdtDetailResponse;
import com.ckb.explorer.domain.resp.UdtHolderAllocationsResponse;
import com.ckb.explorer.domain.resp.UdtTransactionPageResponse;
import com.ckb.explorer.domain.resp.UdtsListResponse;

import java.util.List;

/**
 * IUdtCacheFacade 提供UDT相关的缓存操作接口
 */
public interface IUdtCacheFacade {


    List<UdtHolderAllocationsResponse> findByTypeScriptHash(String typeScriptHash);


    Page<UdtsListResponse> udtListStatistic(UdtPageReq req);

    UdtDetailResponse findDetailByTypeHash(String typeHash);

}