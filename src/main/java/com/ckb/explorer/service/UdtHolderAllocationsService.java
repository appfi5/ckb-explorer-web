package com.ckb.explorer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.req.UdtPageReq;
import com.ckb.explorer.domain.resp.UdtDetailResponse;
import com.ckb.explorer.domain.resp.UdtHolderAllocationsResponse;
import com.ckb.explorer.domain.resp.UdtsListResponse;
import com.ckb.explorer.entity.UdtHolderAllocations;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dell
* @description 针对表【udt_holder_allocations】的数据库操作Service
* @createDate 2025-09-08 17:09:56
*/
public interface UdtHolderAllocationsService extends IService<UdtHolderAllocations> {

    List<UdtHolderAllocationsResponse> findByTypeScriptHash(String typeScriptHash);

    Page<UdtsListResponse> udtListStatistic(UdtPageReq req);

    UdtDetailResponse findDetailByTypeHash(String typeHash);
}
