package com.ckb.explorer.convert;

import com.ckb.explorer.domain.resp.BasicBlockResponse;
import com.ckb.explorer.entity.BasicBlock;
import com.ckb.explorer.utils.CkbHashType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CkbHashType.class})
public interface BasicBlockConvert {

    BasicBlockConvert INSTANCE = Mappers.getMapper(BasicBlockConvert.class);

    @Mapping(source = "parentHash", target = "parentHash", qualifiedByName = "byteArrayToString")
    @Mapping(source = "transactionsRoot", target = "transactionsRoot", qualifiedByName = "byteArrayToString")
    @Mapping(source = "proposalsHash", target = "proposalsHash", qualifiedByName = "byteArrayToString")
    @Mapping(source = "extraHash", target = "extraHash", qualifiedByName = "byteArrayToString")
    @Mapping(source = "blockHash", target = "blockHash", qualifiedByName = "byteArrayToString")
    BasicBlockResponse convert(BasicBlock basicBlock);

    List<BasicBlockResponse> convertList(List<BasicBlock> basicBlocks);

    @Named("byteArrayToString")
    default String byteArrayToString(byte[] bytes) {
        CkbHashType hashType = new CkbHashType("0x");
        return hashType.deserialize(bytes);
    }
}