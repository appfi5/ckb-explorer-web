package com.ckb.explorer.mapstruct;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.dto.CellOutputDto;
import com.ckb.explorer.domain.resp.CellOutputResponse;
import com.ckb.explorer.domain.resp.ScriptResponse;
import com.ckb.explorer.enums.HashType;
import com.ckb.explorer.util.TypeConversionUtil;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {TypeConversionUtil.class})

public interface CellOutputConvert {
  CellOutputConvert INSTANCE = Mappers.getMapper(CellOutputConvert.class);

  @Mapping(target = "addressHash", ignore = true)
  @Mapping(target = "typeScript", ignore = true)
  @Mapping(target = "data",expression = "java(cellOutput.getData() != null? org.nervos.ckb.utils.Numeric.toHexString(cellOutput.getData()): null)")
  CellOutputResponse toConvert(CellOutputDto cellOutput);

  List<CellOutputResponse> toConvertList(List<CellOutputDto> list);

  Page<CellOutputResponse> toConvertPage(Page<CellOutputDto> page);

  @AfterMapping
  default void setAdditionalFields(@MappingTarget CellOutputResponse response, CellOutputDto cellOutput) {
    if(cellOutput.getLockCodeHash() != null && cellOutput.getLockArgs()!=null && cellOutput.getLockHashType()!=null) {
      response.setAddressHash(
          TypeConversionUtil.scriptToAddress(
              cellOutput.getLockCodeHash(),
              cellOutput.getLockArgs(),
              cellOutput.getLockHashType()
          )
      );
    }
    if(cellOutput.getArgs() != null && cellOutput.getCodeHash()!=null && cellOutput.getHashType()!=null){
      response.setTypeScript(new ScriptResponse(cellOutput.getArgs(),cellOutput.getCodeHash(), HashType.getValueByCode(cellOutput.getHashType())));
    }
  }
}
