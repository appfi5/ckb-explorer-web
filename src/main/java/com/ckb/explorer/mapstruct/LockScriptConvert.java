package com.ckb.explorer.mapstruct;

import com.ckb.explorer.domain.dto.CellOutputDto;
import com.ckb.explorer.domain.resp.AddressResponse;
import com.ckb.explorer.domain.resp.CellOutputResponse;
import com.ckb.explorer.domain.resp.LockScriptResponse;
import com.ckb.explorer.domain.resp.ScriptResponse;
import com.ckb.explorer.entity.Script;
import com.ckb.explorer.enums.HashType;
import com.ckb.explorer.util.TypeConversionUtil;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {TypeConversionUtil.class})
public interface LockScriptConvert {
  LockScriptConvert INSTANCE = Mappers.getMapper(LockScriptConvert.class);
  @Mapping(source = "args", target = "args", qualifiedByName = "byteToStringHash(Value)")
  @Mapping(source = "codeHash", target = "codeHash", qualifiedByName = "byteToStringHash(Value)")
  @Mapping(target = "hashType", expression = "java(com.ckb.explorer.enums.HashType.getValueByCode(script.getHashType()))")
  LockScriptResponse toConvert(Script script);

  @Mapping(target = "addressHash", ignore = true)
  AddressResponse toConvertAddressResponse(Script script);


  @AfterMapping
  default void setAdditionalFields(@MappingTarget AddressResponse response, Script script) {
    response.setAddressHash(
          TypeConversionUtil.scriptToAddress(
              script.getCodeHash(),
              script.getArgs(),
              script.getHashType()
          )
    );

    LockScriptResponse lockScriptResponse = new LockScriptResponse();
    lockScriptResponse.setArgs(TypeConversionUtil.byteToStringHash(script.getArgs()));
    lockScriptResponse.setCodeHash(TypeConversionUtil.byteToStringHash(script.getCodeHash()));
    lockScriptResponse.setHashType(HashType.getValueByCode(script.getHashType()));
    response.setLockScript(lockScriptResponse);

  }
}
