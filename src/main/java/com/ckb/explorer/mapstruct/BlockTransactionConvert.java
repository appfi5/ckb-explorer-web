package com.ckb.explorer.mapstruct;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.resp.BlockTransactionPageResponse;
import com.ckb.explorer.entity.CkbTransaction;
import com.ckb.explorer.util.TypeConversionUtil;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {TypeConversionUtil.class})
public interface BlockTransactionConvert {
  BlockTransactionConvert INSTANCE = Mappers.getMapper(BlockTransactionConvert.class);

  @Mapping(target = "isCellbase", expression = "java(ckbTransaction.getTxIndex() == 0)")
  @Mapping(source = "txHash", target = "transactionHash", qualifiedByName = "byteToStringHash(Value)")
  @Mapping(source = "inputCount", target = "displayInputsCount")
  @Mapping(source = "outputCount", target = "displayOutputsCount")
  BlockTransactionPageResponse toConvert(CkbTransaction ckbTransaction);

  List<BlockTransactionPageResponse> toConvertList(List<CkbTransaction> list);

  Page<BlockTransactionPageResponse> toConvertPage(Page<CkbTransaction> page);


}
