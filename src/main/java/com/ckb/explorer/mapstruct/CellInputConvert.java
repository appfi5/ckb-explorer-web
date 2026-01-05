package com.ckb.explorer.mapstruct;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.dto.CellInputDto;
import com.ckb.explorer.domain.dto.PendingCellInputDto;
import com.ckb.explorer.domain.resp.CellInputResponse;
import com.ckb.explorer.domain.resp.ScriptResponse;
import com.ckb.explorer.domain.resp.SinceResponse;
import com.ckb.explorer.enums.HashType;
import com.ckb.explorer.util.TypeConversionUtil;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.nervos.ckb.utils.Numeric;

@Mapper(componentModel = "spring", uses = {TypeConversionUtil.class})
public interface CellInputConvert {
  CellInputConvert INSTANCE = Mappers.getMapper(CellInputConvert.class);

  @Mapping(target = "addressHash", ignore = true)
  @Mapping(target = "since", ignore = true)
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
    response.setSince(new SinceResponse(TypeConversionUtil.convertSince(cellInput.getSinceRaw()),null));
  }

  Page<CellInputResponse> toConvertPendingPage(Page<PendingCellInputDto> page);

  List<CellInputResponse> toConvertPendingList(List<PendingCellInputDto> page);

  @Mapping(target = "addressHash", ignore = true)
  @Mapping(target = "since", ignore = true)
  @Mapping(target = "typeScript", ignore = true)
  @Mapping(target = "generatedTxHash",expression = "java(cellInput.getGeneratedTxHash() != null? org.nervos.ckb.utils.Numeric.toHexString(cellInput.getGeneratedTxHash()): null)")
  @Mapping(target = "data",expression = "java(cellInput.getData() != null? org.nervos.ckb.utils.Numeric.toHexString(cellInput.getData()): null)")
  CellInputResponse toConvertPending(PendingCellInputDto cellInput);

  @AfterMapping
  default void setAdditionalFields(@MappingTarget CellInputResponse response, PendingCellInputDto cellInput) {
    response.setAddressHash(
        TypeConversionUtil.scriptToAddress(
            cellInput.getLockCodeHash(),
            cellInput.getLockArgs(),
            cellInput.getLockHashType()
        )
    );
    if(cellInput.getTypeArgs() != null && cellInput.getTypeCodeHash()!=null && cellInput.getTypeHashType()!=null){
      response.setTypeScript(new ScriptResponse(Numeric.toHexString(cellInput.getTypeArgs()),Numeric.toHexString(cellInput.getTypeCodeHash()), HashType.getValueByCode(cellInput.getTypeHashType())));
    }
    response.setSince(new SinceResponse(TypeConversionUtil.convertSince(cellInput.getSinceRaw()),null));
  }
}
