package com.ckb.explorer.mapstruct;

import com.ckb.explorer.domain.resp.LockScriptResponse;
import com.ckb.explorer.entity.Script;
import com.ckb.explorer.util.TypeConversionUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {TypeConversionUtil.class})
public interface LockScriptConvert {
  LockScriptConvert INSTANCE = Mappers.getMapper(LockScriptConvert.class);
  @Mapping(source = "args", target = "args", qualifiedByName = "byteToStringHash(Value)")
  @Mapping(source = "codeHash", target = "codeHash", qualifiedByName = "byteToStringHash(Value)")
  @Mapping(target = "hashType", expression = "java(com.ckb.explorer.enums.HashType.getValueByCode(script.getHashType()))")
  LockScriptResponse toConvert(Script script);
}
