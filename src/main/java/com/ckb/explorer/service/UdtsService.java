package com.ckb.explorer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.req.UdtsPageReq;
import com.ckb.explorer.domain.resp.UdtDetailResponse;
import com.ckb.explorer.entity.Udts;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author dell
* @description 针对表【udts】的数据库操作Service
* @createDate 2025-09-05 11:01:18
*/
public interface UdtsService extends IService<Udts> {

    Page<Udts> getUdtsPageBy(UdtsPageReq req);

    UdtDetailResponse findUdtDetailByTypeScriptHash(String typeScriptHash);

    Udts findUdtsByTypeScriptHash(String typeScriptHash);
}
