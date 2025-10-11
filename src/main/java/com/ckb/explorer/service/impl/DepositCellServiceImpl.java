package com.ckb.explorer.service.impl;

import com.ckb.explorer.domain.dto.DaoDepositorDto;
import com.ckb.explorer.domain.resp.DaoDepositorResponse;
import com.ckb.explorer.entity.Script;
import com.ckb.explorer.mapper.DepositCellMapper;
import com.ckb.explorer.mapper.ScriptMapper;
import com.ckb.explorer.service.DepositCellService;
import com.ckb.explorer.util.TypeConversionUtil;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class DepositCellServiceImpl implements DepositCellService {

  @Resource
  private DepositCellMapper depositCellMapper;
  @Resource
  private ScriptMapper scriptMapper;
  @Override
  public List<DaoDepositorResponse> getTopDaoDepositors() {

    // 获取前100名DAO存款者列表
    List<DaoDepositorDto> depositors = depositCellMapper.getTopDaoDepositors();
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
}
