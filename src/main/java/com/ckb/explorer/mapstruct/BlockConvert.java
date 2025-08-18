package com.ckb.explorer.mapstruct;

import com.ckb.explorer.domain.resp.BlockListResponse;
import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.ckb.explorer.entity.Block;
import com.ckb.explorer.util.TypeConversionUtil;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {TypeConversionUtil.class})
public interface BlockConvert {

  BlockConvert INSTANCE = Mappers.getMapper(BlockConvert.class);

  @Mapping(source = "id", target = "id", qualifiedByName = "longToString(Value)")
  @Mapping(target = "type", constant = "block_list")
  @Mapping(source = "block", target = "attributes", qualifiedByName = "toBlockListResponse")
  BaseResponse<BlockListResponse> toConvert(Block block);

  List<BaseResponse<BlockListResponse>> toConvertList(List<Block> blocks);

  @Named("toBlockListResponse")
  @Mapping(source = "miner_script", target = "miner_hash", qualifiedByName = "lockScriptToAddress(Value)")
  @Mapping(source = "block_number", target = "number", qualifiedByName = "longToString(Value)")
  @Mapping(source = "timestamp", target = "timestamp", qualifiedByName = "longToString(Value)")
  @Mapping(source = "reward", target = "reward", qualifiedByName = "longToString(Value)")
  @Mapping(source = "transactions_count", target = "transactions_count", qualifiedByName = "integerToString(Value)")
  @Mapping(source = "live_cell_changes", target = "live_cell_changes", qualifiedByName = "integerToString(Value)")
  BlockListResponse toBlockListResponse(Block block);

}
