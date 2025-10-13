package com.ckb.explorer.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.req.CollectionsPageReq;
import com.ckb.explorer.domain.req.NftHoldersPageReq;
import com.ckb.explorer.domain.req.NftTransfersPageReq;
import com.ckb.explorer.domain.req.base.BasePageReq;
import com.ckb.explorer.domain.resp.*;

/**
 * ICollectionsCacheFacade 提供DOB相关的缓存操作接口
 */
public interface INftCacheFacade {


    Page<CollectionsResp> collectionsPage(CollectionsPageReq req);

    CollectionsResp findById(Long id);

    Page<NftTransfersResp> nftTransfersPage(Long collectionId ,NftTransfersPageReq req);

    Page<NftHolderResp> nftHolders(Long collectionId, NftHoldersPageReq req);

    Page<NftItemResponse> nftItems(Long collectionId, BasePageReq req);

    NftItemDetailResponse itemInfo(Long cellId);
}