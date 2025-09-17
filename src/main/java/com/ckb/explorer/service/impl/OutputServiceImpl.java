package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.config.ScriptConfig;
import com.ckb.explorer.domain.resp.CellInfoResponse;
import com.ckb.explorer.domain.resp.ExtraInfoResponse;
import com.ckb.explorer.entity.Output;
import com.ckb.explorer.entity.OutputExtend;
import com.ckb.explorer.entity.Script;
import com.ckb.explorer.enums.CellType;
import com.ckb.explorer.mapper.OutputExtendMapper;
import com.ckb.explorer.mapper.OutputMapper;
import com.ckb.explorer.mapper.ScriptMapper;
import com.ckb.explorer.mapstruct.LockScriptConvert;
import com.ckb.explorer.mapstruct.TypeScriptConvert;
import com.ckb.explorer.service.OutputService;
import com.ckb.explorer.util.CkbUtil;
import com.ckb.explorer.util.TypeConversionUtil;
import jakarta.annotation.Resource;
import java.util.Set;
import org.nervos.ckb.utils.Numeric;
import org.springframework.stereotype.Service;

@Service
public class OutputServiceImpl extends ServiceImpl<OutputMapper, Output> implements
    OutputService {

  @Resource
  private OutputExtendMapper outputExtendMapper;

  @Resource
  private ScriptMapper scriptMapper;

  @Resource
  private ScriptConfig scriptConfig;

  @Override
  public Long countAddressTransactions(Long scriptId) {
    return baseMapper.countAddressTransactions(scriptId);
  }

  @Override
  public CellInfoResponse getCellInfo(Long id) {
    CellInfoResponse response = new CellInfoResponse();
    // 根据ID查询CellOutput
    Output cellOutput = baseMapper.selectById(id);
    if (cellOutput == null) {
      return null;
    }
    response.setId(cellOutput.getId());
    response.setCapacity(cellOutput.getCapacity());
    response.setOccupiedCapacity(cellOutput.getOccupiedCapacity());
    response.setStatus(cellOutput.getIsSpent());
    response.setCellIndex(cellOutput.getOutputIndex());
    response.setGeneratedTxHash(cellOutput.getTxHash() != null? Numeric.toHexString(cellOutput.getTxHash()) : null);
    response.setConsumedTxHash(cellOutput.getConsumedTxHash() != null && cellOutput.getConsumedTxHash().length > 0? Numeric.toHexString(cellOutput.getConsumedTxHash()) : null);

    response.setData(cellOutput.getData() != null ? Numeric.toHexString(cellOutput.getData()) : null);

    // 获取cellType
    OutputExtend outputExtend = outputExtendMapper.selectOne(new QueryWrapper<OutputExtend>().eq("output_id", id));
    Integer cellType = null;
    // 只有解析过的output才有cellType
    if(outputExtend != null){
      cellType = outputExtend.getCellType();
      // ckb的cellType默认为0
    } else if(cellOutput.getTypeScriptId() == null){
      cellType = CellType.NORMAL.getValue();
    }
    response.setCellType(cellType);

    // 获取lock_script
    Script lockScript = scriptMapper.selectById(cellOutput.getLockScriptId());
    if(lockScript == null){
      response.setLockScript(null);
    }else{
      var lockScriptResponse = LockScriptConvert.INSTANCE.toConvert(lockScript);
      var script = scriptConfig.getLockScriptByCodeHash(lockScriptResponse.getCodeHash());
      lockScriptResponse.setVerifiedScriptName(script == null ? null : script.getName());
      response.setLockScript(lockScriptResponse);
      response.setAddress(TypeConversionUtil.scriptToAddress(lockScript.getCodeHash(),
          lockScript.getArgs(),
          lockScript.getHashType()));
    }

    // 获取type_script
    if(cellOutput.getTypeScriptId() != null){
      Script typeScript = scriptMapper.selectById(cellOutput.getTypeScriptId());
      if (typeScript != null) {
        var typeScriptResponse = TypeScriptConvert.INSTANCE.toConvert(typeScript);
        var script = scriptConfig.getTypeScriptByCodeHash(typeScriptResponse.getCodeHash(), typeScriptResponse.getArgs());
        typeScriptResponse.setVerifiedScriptName(script == null ? null : script.getName());
        response.setTypeScript(typeScriptResponse);

        // 只有资产才有扩展信息
        if(cellType != null){
          var extraInfoResponse = new ExtraInfoResponse();
          extraInfoResponse.setType(CellType.getTypeByCellType(cellType));
          // 根据不同的cellType组装不同的扩展信息
          Set<Integer> udtCellType = Set.of(CellType.UDT.getValue(),CellType.XUDT.getValue(),CellType.XUDT_COMPATIBLE.getValue(),CellType.SSRI.getValue());
          if(udtCellType.contains(cellType.intValue()) && script != null){
            extraInfoResponse.setSymbol(script.getSymbol());
            extraInfoResponse.setDecimal(script.getDecimal());
            extraInfoResponse.setTypeHash(Numeric.toHexString(typeScript.getScriptHash()));
            extraInfoResponse.setAmount(CkbUtil.dataToUdtAmount(cellOutput.getData()));
            extraInfoResponse.setPublished(true);
          }

          response.setExtraInfo(extraInfoResponse);
        }
      }
    }

    return response;
  }
}
