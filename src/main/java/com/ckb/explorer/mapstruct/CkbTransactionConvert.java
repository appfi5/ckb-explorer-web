package com.ckb.explorer.mapstruct;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.dto.TransactionDto;
import com.ckb.explorer.domain.resp.AddressTransactionPageResponse;
import com.ckb.explorer.domain.resp.TransactionPageResponse;
import com.ckb.explorer.domain.resp.TransactionResponse;
import com.ckb.explorer.domain.resp.UdtTransactionPageResponse;
import com.ckb.explorer.entity.CkbTransaction;
import com.ckb.explorer.util.TypeConversionUtil;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {TypeConversionUtil.class})
public interface CkbTransactionConvert {
  CkbTransactionConvert INSTANCE = Mappers.getMapper(CkbTransactionConvert.class);

  @Mapping(source = "txHash", target = "transactionHash", qualifiedByName = "byteToStringHash(Value)")
  @Mapping(expression = "java(ckbTransaction.getTxIndex()==0? 1: ckbTransaction.getOutputCount() - ckbTransaction.getInputCount())", target = "liveCellChanges")
  TransactionPageResponse toConvert(CkbTransaction ckbTransaction);

  List<TransactionPageResponse> toConvertTransactionResponseList(List<CkbTransaction> ckbTransactions);

  Page<TransactionPageResponse> toConvertPage(Page<CkbTransaction> page);

  @Mapping(source = "witnesses", target = "witnesses", qualifiedByName = "byteToWitnesses(Value)")
  @Mapping(source = "headerDeps", target = "headerDeps", qualifiedByName = "byteToHashList(Value)")
  @Mapping(source = "txHash", target = "transactionHash")
  TransactionResponse toConvertTransactionResponse(TransactionDto ckbTransaction);


  List<UdtTransactionPageResponse> toConvertUdtTransactionList(List<AddressTransactionPageResponse> page);

}
