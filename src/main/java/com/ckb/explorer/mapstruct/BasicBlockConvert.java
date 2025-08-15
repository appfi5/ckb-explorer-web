package com.ckb.explorer.mapstruct;

import com.ckb.explorer.domain.resp.BasicBlockResponse;
import com.ckb.explorer.entity.BasicBlock;
import com.ckb.explorer.utils.CkbArrayHashType;
import com.ckb.explorer.utils.CkbHashType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CkbHashType.class, CkbArrayHashType.class})
public interface BasicBlockConvert {

  BasicBlockConvert INSTANCE = Mappers.getMapper(BasicBlockConvert.class);

  @Mapping(source = "parent_hash", target = "parent_hash", qualifiedByName = "byteToString")
  @Mapping(source = "transactions_root", target = "transactions_root", qualifiedByName = "byteToString")
  @Mapping(source = "proposals_hash", target = "proposals_hash", qualifiedByName = "byteToString")
  @Mapping(source = "extra_hash", target = "extra_hash", qualifiedByName = "byteToString")
  @Mapping(source = "block_hash", target = "block_hash", qualifiedByName = "byteToString")
  @Mapping(source = "extension", target = "extension", qualifiedByName = "byteToString")
  @Mapping(source = "proposals", target = "proposals", qualifiedByName = "byteArrayToListString")
  BasicBlockResponse convert(BasicBlock basicBlock);

  List<BasicBlockResponse> convertList(List<BasicBlock> basicBlocks);

  @Named("byteToString")
  default String byteToString(byte[] bytes) {
    CkbHashType hashType = new CkbHashType("0x");
    return hashType.deserialize(bytes);
  }

  @Named("byteArrayToListString")
  default List<String> byteArrayToString(byte[] bytes) {
    CkbArrayHashType arrayHashType = new CkbArrayHashType("0x", 10);
    return arrayHashType.deserialize(bytes);
  }
}