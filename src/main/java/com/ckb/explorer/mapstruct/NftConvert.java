package com.ckb.explorer.mapstruct;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.dto.NftHolderDto;
import com.ckb.explorer.domain.dto.NftItemDto;
import com.ckb.explorer.domain.dto.NftTransfersDto;
import com.ckb.explorer.domain.resp.NftHolderResp;
import com.ckb.explorer.domain.resp.NftItemResponse;
import com.ckb.explorer.domain.resp.NftTransfersResp;
import com.ckb.explorer.util.TypeConversionUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {TypeConversionUtil.class})
public interface NftConvert {

    NftConvert INSTANCE = Mappers.getMapper(NftConvert.class);

    @Mapping(source = "txHash", target = "txHash", qualifiedByName = "byteToStringHash(Value)")
    NftTransfersResp toNftTransfersResp(NftTransfersDto nftTransfersDto);

    Page<NftTransfersResp> toNftTransfersRespPage(Page<NftTransfersDto> page);

    Page<NftHolderResp> toNftHoldersRespPage(Page<NftHolderDto> page);

    @Mapping(source = "data", target = "data", qualifiedByName = "byteToStringHash(Value)")
    NftItemResponse toNftItemResp(NftItemDto nftItemDto);

    Page<NftItemResponse> toNftItemsRespPage(Page<NftItemDto> page);

}
