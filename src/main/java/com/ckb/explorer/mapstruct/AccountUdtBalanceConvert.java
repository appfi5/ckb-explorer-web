package com.ckb.explorer.mapstruct;

import com.ckb.explorer.domain.dto.AccountUdtBalanceDto;
import com.ckb.explorer.domain.resp.AccountUdtBalanceResponse;
import com.ckb.explorer.enums.HashType;
import com.ckb.explorer.util.TypeConversionUtil;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {TypeConversionUtil.class})
public interface AccountUdtBalanceConvert {
  AccountUdtBalanceConvert INSTANCE = Mappers.getMapper(AccountUdtBalanceConvert.class);

  AccountUdtBalanceResponse toConvert(AccountUdtBalanceDto accountUdtBalanceDto);

  List<AccountUdtBalanceResponse> toConvert(List<AccountUdtBalanceDto> accountUdtBalanceDtos);

  @AfterMapping
  default void afterMapping(@MappingTarget AccountUdtBalanceResponse accountUdtBalanceResponse, AccountUdtBalanceDto accountUdtBalanceDto)
  {
    var hashType = Short.valueOf(accountUdtBalanceDto.getUdtTypeScript().getHashType());
    accountUdtBalanceResponse.getUdtTypeScript().setHashType(HashType.getValueByCode(hashType));
  }
}
