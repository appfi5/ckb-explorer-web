package com.ckb.explorer.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.req.CollectionsPageReq;
import com.ckb.explorer.domain.resp.CollectionsResp;

/**
 * ICollectionsCacheFacade 提供DOB相关的缓存操作接口
 */
public interface INftCacheFacade {


    Page<CollectionsResp> page(CollectionsPageReq req);
}