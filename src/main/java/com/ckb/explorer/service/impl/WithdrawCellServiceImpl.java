package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.domain.dto.DaoCellDto;
import com.ckb.explorer.entity.WithdrawCell;
import com.ckb.explorer.mapper.WithdrawCellMapper;
import com.ckb.explorer.service.BlockService;
import com.ckb.explorer.service.WithdrawCellService;
import com.ckb.explorer.util.CkbUtil;
import com.ckb.explorer.util.DaoCompensationCalculator;
import jakarta.annotation.Resource;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class WithdrawCellServiceImpl extends
    ServiceImpl<WithdrawCellMapper, WithdrawCell> implements WithdrawCellService {

  @Resource
  private BlockService blockService;

  /**
   * 计算第一阶段DAO利息
   *
   * @param lockScriptId 锁脚本ID
   * @return 第一阶段DAO利息总和
   */
  @Override
  public BigInteger phase1DaoInterestsByLockScriptId(Long lockScriptId) {
    BigInteger total = BigInteger.ZERO;
    try {
      // 查询未消耗的WithdrawCell
      List<DaoCellDto> nervosDaoWithdrawingCells = baseMapper.getUnConsumedCellsByLockScriptId(lockScriptId);
      if(nervosDaoWithdrawingCells.isEmpty()){
        return total;
      }
      Set<Long> depositBlockNumbers = nervosDaoWithdrawingCells.stream()
          .map(withdrawCell -> CkbUtil.convertToBlockNumber(withdrawCell.getData())).collect(
              Collectors.toSet());
      Set<Long> withdrawBlockNumbers = nervosDaoWithdrawingCells.stream()
          .map(DaoCellDto::getBlockNumber).collect(Collectors.toSet());
      depositBlockNumbers.addAll(withdrawBlockNumbers);

      Map<Long, byte[]> blockDaos = blockService.getBlockDaos(depositBlockNumbers);

      // 计算每个cell的DAO利息
      for (DaoCellDto cell : nervosDaoWithdrawingCells) {
        total = total.add(DaoCompensationCalculator.call(cell,
            blockDaos.get(cell.getBlockNumber()),
            blockDaos.get(CkbUtil.convertToBlockNumber(cell.getData()))));
      }
    } catch (Exception e) {
      log.error("计算阶段1DAO利息异常", e);
    }
    return total;
  }
}
