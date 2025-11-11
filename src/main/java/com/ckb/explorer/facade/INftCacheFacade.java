package com.ckb.explorer.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.req.CollectionsPageReq;
import com.ckb.explorer.domain.req.NftHoldersPageReq;
import com.ckb.explorer.domain.req.NftTransfersPageReq;
import com.ckb.explorer.domain.req.base.BasePageReq;
import com.ckb.explorer.domain.resp.*;

import java.util.List;

/**
 * ICollectionsCacheFacade 提供DOB相关的缓存操作接口
 */
public interface INftCacheFacade {


    Page<CollectionsResp> collectionsPage(CollectionsPageReq req);

    CollectionsResp findByTypeScriptHash(String typeScriptHash);

    Page<NftTransfersResp> nftTransfersPage(String typeScriptHash ,NftTransfersPageReq req);

    Page<NftHolderResp> nftHolders(String typeScriptHash, NftHoldersPageReq req);

    Page<NftItemResponse> nftItems(String typeScriptHash, BasePageReq req);

    NftItemDetailResponse itemInfo(String typeScriptHash, String tokenId);

    List<AccountNftResponse> accountNftResponses(String address);
}