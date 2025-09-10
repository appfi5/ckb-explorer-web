package com.ckb.explorer.mapstruct;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.req.FungibleTokensPageReq;
import com.ckb.explorer.domain.req.SudtsPageReq;
import com.ckb.explorer.domain.req.UdtsPageReq;
import com.ckb.explorer.domain.req.XudtsPageReq;
import com.ckb.explorer.domain.resp.*;
import com.ckb.explorer.entity.UdtHolderAllocations;
import com.ckb.explorer.entity.Udts;
import com.ckb.explorer.util.TypeConversionUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TypeConversionUtil.class})
public interface UdtsConvert {
    UdtsConvert INSTANCE = Mappers.getMapper(UdtsConvert.class);
    UdtsPageReq  fungibletoUdtsPageReq(FungibleTokensPageReq req);


    Page<FungibleTokensPageResponse> udtsPagetoFungibleTokensPage(Page<Udts> page);

    @Mapping(source = "typeScriptHash", target = "typeScriptHash", qualifiedByName = "byteToStringHash(Value)")
    FungibleTokensPageResponse udtstoFungibleToken(Udts udts);

    @Mapping(source = "typeScriptHash", target = "typeScriptHash", qualifiedByName = "byteToStringHash(Value)")
    @Mapping(source = "issuerAddress", target = "issuerAddress", qualifiedByName = "byteToStringHash(Value)")
    UdtDetailResponse udtsToUdtDetailResponse(Udts udts);


    Page<XudtsPageResponse> udtsPagetoXudtsPage(Page<Udts> page);

    @Mapping(source = "typeScriptHash", target = "typeScriptHash", qualifiedByName = "byteToStringHash(Value)")
    XudtsPageResponse udtstoXudtsPageResponse(Udts udts);


    Page<SudtsPageResponse> udtsPagetoSudtsPage(Page<Udts> page);

    @Mapping(source = "typeScriptHash", target = "typeScriptHash", qualifiedByName = "byteToStringHash(Value)")
    SudtsPageResponse udtstoSudtsPageResponse(Udts udts);


    UdtsPageReq xudtsPageReqtoUdtsPageReq(XudtsPageReq req);
    UdtsPageReq sudtsPageReqtoUdtsPageReq(SudtsPageReq req);

    @Mapping(source = "lockCodeHash", target = "lockCodeHash", qualifiedByName = "byteToStringHash(Value)")
    UdtHolderAllocationsResponse udtHoldertoResponse(UdtHolderAllocations udtHolderAllocations);


    List<UdtHolderAllocationsResponse> udtHolderListtoResponse(List<UdtHolderAllocations> udtHolderAllocations);
}
