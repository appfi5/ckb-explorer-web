package com.ckb.explorer.mapstruct;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.CollectionsDto;
import com.ckb.explorer.domain.dto.AccountNftDto;
import com.ckb.explorer.domain.dto.NftHolderDto;
import com.ckb.explorer.domain.dto.NftItemDto;
import com.ckb.explorer.domain.dto.NftTransfersDto;
import com.ckb.explorer.domain.resp.*;
import com.ckb.explorer.util.TypeConversionUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TypeConversionUtil.class})
public interface NftConvert {

    NftConvert INSTANCE = Mappers.getMapper(NftConvert.class);

    @Mapping(source = "action", target = "action", qualifiedByName = "nftAction(Value)")
    @Mapping(source = "txHash", target = "txHash", qualifiedByName = "byteToStringHash(Value)")
    @Mapping(source = "data", target = "data", qualifiedByName = "byteToStringHash(Value)")
    @Mapping(source = "standard", target = "standard", qualifiedByName = "nftType(Value)")
    NftTransfersResp toNftTransfersResp(NftTransfersDto nftTransfersDto);

    Page<NftTransfersResp> toNftTransfersRespPage(Page<NftTransfersDto> page);

    Page<NftHolderResp> toNftHoldersRespPage(Page<NftHolderDto> page);

    @Mapping(source = "data", target = "data", qualifiedByName = "byteToStringHash(Value)")
    @Mapping(source = "standard", target = "standard", qualifiedByName = "nftType(Value)")
    NftItemResponse toNftItemResp(NftItemDto nftItemDto);

    Page<NftItemResponse> toNftItemsRespPage(Page<NftItemDto> page);

    @Mapping(source = "args", target = "clusterId", qualifiedByName = "byteToStringHash(Value)")
    @Mapping(source = "dobScriptHash", target = "typeScriptHash", qualifiedByName = "byteToStringHash(Value)")
    @Mapping(source = "standard", target = "standard", qualifiedByName = "nftType(Value)")
    CollectionsResp toCollectionsResp(CollectionsDto collectionsDto);

    @Mapping(source = "data", target = "data", qualifiedByName = "byteToStringHash(Value)")
    @Mapping(source = "standard", target = "standard", qualifiedByName = "nftType(Value)")
    NftItemDetailResponse toNftItemDetailResp(NftItemDto nftItemDto);


    Page<CollectionsResp> toNftCollectionsRespPage(Page<CollectionsDto> page);


    @Mapping(source = "dobScriptHash", target = "collectionTypeHash", qualifiedByName = "byteToStringHash(Value)")
    @Mapping(source = "dobCodeScriptArgs", target = "tokenId", qualifiedByName = "byteToStringHash(Value)")
    @Mapping(source = "data", target = "nftIconFile", qualifiedByName = "byteToStringHash(Value)")
    AccountNftResponse toAccountNftResponse(AccountNftDto dto);

    List<AccountNftResponse> toAccountNftResponseList(List<AccountNftDto> dtos);


}
