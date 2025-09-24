package com.ckb.explorer.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ckb.explorer.domain.dto.AccountUdtBalanceDto;
import com.ckb.explorer.domain.dto.UdtAddressCountDto;
import com.ckb.explorer.entity.UdtAccounts;

import java.math.BigInteger;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
* @author dell
* @description 针对表【udt_accounts】的数据库操作Mapper
* @createDate 2025-08-29 15:02:45
* @Entity
*/
@DS("risingwave")
public interface UdtAccountsMapper extends BaseMapper<UdtAccounts> {

  List<AccountUdtBalanceDto> getUdtBalanceByLockScriptId(@Param("lockScriptId") Long lockScriptId);

  List<UdtAddressCountDto> getAddressNumByScriptHashes(@Param("scriptHashes") List<byte[]> scriptHashes);

  @Select("select sum(amount) from udt_accounts where type_script_id = #{typeScriptId} ")
  BigInteger getTotalAmountByTypeScriptId(@Param("typeScriptId") Long typeScriptId);
}




