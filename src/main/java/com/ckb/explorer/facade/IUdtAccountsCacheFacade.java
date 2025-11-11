package com.ckb.explorer.facade;

import com.ckb.explorer.domain.resp.AccountUdtBalanceResponse;
import java.util.List;

public interface IUdtAccountsCacheFacade {

  List<AccountUdtBalanceResponse> getUdtBalance(String address);
}
