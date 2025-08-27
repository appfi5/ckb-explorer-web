package com.ckb.explorer.mapstruct;

import com.ckb.explorer.domain.resp.BlockchainInfoResponse;
import com.ckb.explorer.util.TypeConversionUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.nervos.ckb.type.BlockchainInfo;

@Mapper(componentModel = "spring", uses = {TypeConversionUtil.class})
public interface BlockchainInfoConvert {
  BlockchainInfoConvert INSTANCE = Mappers.getMapper(BlockchainInfoConvert.class);

  @Mapping(target = "difficulty", expression = "java(org.nervos.ckb.utils.Numeric.toBigInt(blockchainInfo.difficulty))")
  BlockchainInfoResponse toConvert(BlockchainInfo blockchainInfo);
}
