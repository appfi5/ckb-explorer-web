package com.ckb.explorer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ckb.explorer.domain.resp.AccountUdtBalanceResponse;
import com.ckb.explorer.entity.UdtAccounts;
import java.util.List;

/**
* @author dell
* @description 针对表【udt_accounts】的数据库操作Service
* @createDate 2025-08-27 14:46:49
*/
public interface UdtAccountsService extends IService<UdtAccounts> {

  List<AccountUdtBalanceResponse> getUdtBalanceByAddress(String address);
}
