package com.ckb.explorer.mapstruct;


import com.ckb.explorer.domain.resp.UdtHolderAllocationsResponse;
import com.ckb.explorer.entity.UdtHolderAllocations;
import com.ckb.explorer.util.TypeConversionUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TypeConversionUtil.class})
public interface UdtHolderAllocationsConvert {

    UdtHolderAllocationsConvert INSTANCE = Mappers.getMapper(UdtHolderAllocationsConvert.class);


    List<UdtHolderAllocationsResponse> udtHolderListtoResponse(List<UdtHolderAllocations> udtHolderAllocations);

    @Mapping(source = "lockCodeHash", target = "lockCodeHash", qualifiedByName = "byteToStringHash(Value)")
    UdtHolderAllocationsResponse toUdtHolderAllocationsResponse(UdtHolderAllocations udtHolderAllocation);
}
