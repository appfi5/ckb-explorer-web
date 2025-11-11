package com.ckb.explorer.service;

import java.math.BigInteger;

public interface WithdrawCellService {
  /**
   * 获取个人DAO存款的阶段1利息
   *
   * @param lockScriptId
   * @return
   */
  BigInteger phase1DaoInterestsByLockScriptId(Long lockScriptId);

}
