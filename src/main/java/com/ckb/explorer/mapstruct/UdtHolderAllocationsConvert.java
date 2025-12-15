package com.ckb.explorer.mapstruct;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.dto.UdtAddressCountDto;
import com.ckb.explorer.domain.resp.UdtHolderAllocationsResponse;
import com.ckb.explorer.domain.resp.UdtsListResponse;
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

    UdtHolderAllocationsResponse toUdtHolderAllocationsResponse(UdtHolderAllocations udtHolderAllocation);

    Page<UdtsListResponse> toUdtPage(Page<UdtAddressCountDto> page);
}
