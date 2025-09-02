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


  @Mapping(source = "blockHash", target = "blockHash", qualifiedByName = "byteToStringHash(Value)")
  @Mapping(source = "uncleBlockHashes", target = "uncleBlockHashes", qualifiedByName = "byteToHashList(Value)")
  @Mapping(source = "minerScript", target = "minerHash", qualifiedByName = "lockScriptToAddress(Value)")
  @Mapping(source = "transactionsRoot", target = "transactionsRoot", qualifiedByName = "byteToStringHash(Value)")
  @Mapping(source = "blockNumber", target = "number")
  @Mapping(source = "epochLength", target = "length")
  @Mapping(source = "version", target = "version", qualifiedByName = "byteToString(Value)")
  @Mapping(source = "epochNumber", target = "epoch")
  @Mapping(target = "blockIndexInEpoch",expression = "java(block.getBlockNumber()-block.getStartNumber())")
  @Mapping(source = "nonce", target = "nonce", qualifiedByName = "byteToString(Value)")
  @Mapping(target = "difficulty", expression = "java(org.nervos.ckb.utils.Numeric.toBigInt(block.getDifficulty()))")
  @Mapping(source = "blockSize", target = "size")
  @Mapping(source = "minerMessage", target = "minerMessage", qualifiedByName = "byteToStringHash(Value)")
  BlockResponse toConvertBlockResponse(Block block);

}
