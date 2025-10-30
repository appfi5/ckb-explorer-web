package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.domain.dto.DaoCellDto;
import com.ckb.explorer.domain.dto.DaoDepositorDto;
import com.ckb.explorer.domain.resp.DaoDepositorResponse;
import com.ckb.explorer.entity.DepositCell;
import com.ckb.explorer.entity.Script;
import com.ckb.explorer.mapper.DepositCellMapper;
import com.ckb.explorer.mapper.ScriptMapper;
import com.ckb.explorer.service.BlockService;
import com.ckb.explorer.service.DepositCellService;
import com.ckb.explorer.util.DaoCompensationCalculator;
import com.ckb.explorer.util.TypeConversionUtil;
import jakarta.annotation.Resource;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class DepositCellServiceImpl extends ServiceImpl<DepositCellMapper, DepositCell> implements DepositCellService {

  @Resource
  private ScriptMapper scriptMapper;

  @Resource
  private BlockService blockService;

  @Override
  public List<DaoDepositorResponse> getTopDaoDepositors() {

    // 获取前100名DAO存款者列表
    List<DaoDepositorDto> depositors = baseMapper.getTopDaoDepositors();
    if (depositors == null || depositors.isEmpty()) {
      return List.of();
    }

      List<Long> lockScriptIds = depositors.stream().map(DaoDepositorDto::getLockScriptId).distinct().toList();
      List<Script> scripts = scriptMapper.selectByIds(lockScriptIds);
      Map<Long, Script> scriptMap = scripts.stream()
        .collect(Collectors.toMap(
            Script::getId,  // key: lockScriptId
            Function.identity(),  // value: Script对象
            (existing, replacement) -> existing  // 处理重复ID（按业务取第一个）
        ));
      return depositors.stream().map(depositor -> {
        Script script = scriptMap.get(depositor.getLockScriptId());
        String address = script != null
            ? TypeConversionUtil.scriptToAddress(script.getCodeHash(), script.getArgs(), script.getHashType())
            : "";
        DaoDepositorResponse response = new DaoDepositorResponse();
        response.setId(depositor.getLockScriptId());
        response.setAddressHash(address);
        response.setDaoDeposit(depositor.getDaoDeposit());
        return response;
      }).toList();
  }

  @Override
  public BigInteger getDepositByLockScriptId(Long lockScriptId) {
    return baseMapper.getDepositByLockScriptId(lockScriptId);
  }

  /**
   * 计算未生成的DAO利息
   *
   * @param lockScriptId 锁脚本ID
   * @return 未生成的DAO利息
   */
  @Override
  public BigInteger unmadeDaoInterestsByLockScriptId(Long lockScriptId) {
    BigInteger total = BigInteger.ZERO;
    try {
      // 查询未消耗的depositCell
      List<DaoCellDto> nervosDaoDepositCells = baseMapper.getUnConsumedCellsByLockScriptId(lockScriptId);
      if(nervosDaoDepositCells.isEmpty()){
        return total;
      }
      Set<Long> depositBlockNumbers = nervosDaoDepositCells.stream().map(DaoCellDto::getBlockNumber)
          .collect(Collectors.toSet());
      Long maxBlockNumber = blockService.getMaxBlockNumber();

      depositBlockNumbers.add(maxBlockNumber);

      Map<Long, byte[]> blockDaos = blockService.getBlockDaos(depositBlockNumbers);

      // 计算每个cell的DAO利息
      for (DaoCellDto cell : nervosDaoDepositCells) {
        total = total.add(DaoCompensationCalculator.call(cell, blockDaos.get(maxBlockNumber),
            blockDaos.get(cell.getBlockNumber())));
      }
    } catch (Exception e) {
      log.error("计算未生成的DAO利息异常", e);
    }
    return total;
  }
}
