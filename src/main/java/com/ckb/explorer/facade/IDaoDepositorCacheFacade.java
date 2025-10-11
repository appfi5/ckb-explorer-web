package com.ckb.explorer.facade;

import com.ckb.explorer.domain.resp.DaoDepositorResponse;
import java.util.List;

public interface IDaoDepositorCacheFacade {
  List<DaoDepositorResponse> getTopDaoDepositors();
}
