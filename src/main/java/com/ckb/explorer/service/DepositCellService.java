package com.ckb.explorer.service;

import com.ckb.explorer.domain.resp.DaoDepositorResponse;
import java.math.BigInteger;
import java.util.List;

public interface DepositCellService {
  /**
   * 获取DAO存款者列表
   *
   * @return
   */
  List<DaoDepositorResponse> getTopDaoDepositors();

  /**
   * 获取个人DAO存款
   *
   * @param lockScriptId
   * @return
   */
  BigInteger getDepositByLockScriptId(Long lockScriptId);

  /**
   * 获取个人未生成的DAO利息
   *
   * @param lockScriptId
   * @return
   */
  BigInteger unmadeDaoInterestsByLockScriptId(Long lockScriptId);
}
