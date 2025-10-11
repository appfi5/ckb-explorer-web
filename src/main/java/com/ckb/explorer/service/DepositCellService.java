package com.ckb.explorer.service;

import com.ckb.explorer.domain.resp.DaoDepositorResponse;
import java.util.List;

public interface DepositCellService {
  List<DaoDepositorResponse> getTopDaoDepositors();
}
