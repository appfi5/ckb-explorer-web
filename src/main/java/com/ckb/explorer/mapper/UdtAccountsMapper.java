package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ckb.explorer.domain.dto.AccountUdtBalanceDto;
import com.ckb.explorer.entity.UdtAccounts;
import java.util.List;
import org.apache.ibatis.annotations.Param;


/**
* @author dell
* @description 针对表【udt_accounts】的数据库操作Mapper
* @createDate 2025-08-29 15:02:45
* @Entity
*/
public interface UdtAccountsMapper extends BaseMapper<UdtAccounts> {

  List<AccountUdtBalanceDto> getUdtBalanceByLockScriptId(@Param("lockScriptId") Long lockScriptId);
}




