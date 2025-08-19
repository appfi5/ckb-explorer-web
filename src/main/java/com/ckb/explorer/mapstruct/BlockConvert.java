package com.ckb.explorer.mapstruct;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.resp.BlockListResponse;
import com.ckb.explorer.domain.resp.BlockResponse;
import com.ckb.explorer.entity.Block;
import com.ckb.explorer.util.TypeConversionUtil;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {TypeConversionUtil.class})
public interface BlockConvert {

  BlockConvert INSTANCE = Mappers.getMapper(BlockConvert.class);

  @Mapping(source = "minerScript", target = "minerHash", qualifiedByName = "lockScriptToAddress(Value)")
  @Mapping(source = "blockNumber", target = "number")
  BlockListResponse toConvert(Block block);

  List<BlockListResponse> toConvertList(List<Block> blocks);

  Page<BlockListResponse> toConvertPage(Page<Block> page);


  @Mapping(source = "minerScript", target = "minerHash", qualifiedByName = "lockScriptToAddress(Value)")
  @Mapping(source = "blockNumber", target = "number")
  @Mapping(source = "blockHash", target = "blockHash", qualifiedByName = "byteToString(Value)")
  @Mapping(source = "transactionsRoot", target = "transactionsRoot", qualifiedByName = "byteToString(Value)")
  @Mapping(source = "version", target = "version", qualifiedByName = "byteToString(Value)")
  @Mapping(source = "epoch", target = "epoch", qualifiedByName = "byteToString(Value)")
  @Mapping(source = "nonce", target = "nonce", qualifiedByName = "byteToString(Value)")
  @Mapping(source = "difficulty", target = "difficulty", qualifiedByName = "byteToString(Value)")
  BlockResponse toConvertBlockResponse(Block block);

}
