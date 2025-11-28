package com.ckb.explorer.mapstruct;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.dto.CellInputDto;
import com.ckb.explorer.domain.resp.CellInputResponse;
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
public interface CellInputConvert {
  CellInputConvert INSTANCE = Mappers.getMapper(CellInputConvert.class);

  @Mapping(target = "addressHash", ignore = true)
  @Mapping(expression = "java(new com.ckb.explorer.domain.resp.SinceResponse(cellInput.getSinceRaw(),null))", target = "since")
  @Mapping(target = "typeScript", ignore = true)
  @Mapping(target = "data",expression = "java(cellInput.getData() != null? org.nervos.ckb.utils.Numeric.toHexString(cellInput.getData()): null)")
  CellInputResponse toConvert(CellInputDto cellInput);

  List<CellInputResponse> toConvertList(List<CellInputDto> list);

  Page<CellInputResponse> toConvertPage(Page<CellInputDto> page);

  @AfterMapping
  default void setAdditionalFields(@MappingTarget CellInputResponse response, CellInputDto cellInput) {
    response.setAddressHash(
        TypeConversionUtil.scriptToAddress(
            cellInput.getLockCodeHash(),
            cellInput.getLockArgs(),
            cellInput.getLockHashType()
        )
    );
    if(cellInput.getArgs() != null && cellInput.getCodeHash()!=null && cellInput.getHashType()!=null){
      response.setTypeScript(new ScriptResponse(cellInput.getArgs(),cellInput.getCodeHash(), HashType.getValueByCode(cellInput.getHashType())));
    }
  }
}
