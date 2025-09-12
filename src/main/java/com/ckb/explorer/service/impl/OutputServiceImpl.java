package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.domain.resp.CellInfoResponse;
import com.ckb.explorer.entity.Output;
import com.ckb.explorer.entity.OutputExtend;
import com.ckb.explorer.entity.Script;
import com.ckb.explorer.mapper.OutputExtendMapper;
import com.ckb.explorer.mapper.OutputMapper;
import com.ckb.explorer.mapper.ScriptMapper;
import com.ckb.explorer.mapstruct.LockScriptConvert;
import com.ckb.explorer.mapstruct.TypeScriptConvert;
import com.ckb.explorer.service.OutputService;
import com.ckb.explorer.util.TypeConversionUtil;
import jakarta.annotation.Resource;
import org.nervos.ckb.utils.Numeric;
import org.springframework.stereotype.Service;

@Service
public class OutputServiceImpl extends ServiceImpl<OutputMapper, Output> implements
    OutputService {

  @Resource
  private OutputExtendMapper outputExtendMapper;

  @Resource
  private ScriptMapper scriptMapper;

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
    response.setCapacity(cellOutput.getCapacity());
    response.setOccupiedCapacity(cellOutput.getOccupiedCapacity());
    response.setStatus(cellOutput.getIsSpent());
    response.setCellIndex(cellOutput.getOutputIndex());
    response.setGeneratedTxHash(cellOutput.getTxHash() != null? Numeric.toHexString(cellOutput.getTxHash()) : null);
    response.setConsumedTxHash(cellOutput.getConsumedTxHash() != null && cellOutput.getConsumedTxHash().length > 0? Numeric.toHexString(cellOutput.getConsumedTxHash()) : null);

    response.setData(cellOutput.getData() != null ? Numeric.toHexString(cellOutput.getData()) : null);

    OutputExtend outputExtend = outputExtendMapper.selectOne(new QueryWrapper<OutputExtend>().eq("output_id", id));
    if(outputExtend != null){
      response.setCellType(outputExtend.getCellType());
    }

    // 获取lock_script
    Script lockScript = scriptMapper.selectById(cellOutput.getLockScriptId());
    if(lockScript == null){
      response.setLockScript(null);
    }else{
      response.setLockScript(LockScriptConvert.INSTANCE.toConvert(lockScript));
      response.setAddress(TypeConversionUtil.scriptToAddress(lockScript.getCodeHash(),
          lockScript.getArgs(),
          lockScript.getHashType()));
    }


    // 获取type_script
    if(cellOutput.getTypeScriptId() != null){
      Script typeScript = scriptMapper.selectById(cellOutput.getTypeScriptId());
      if (typeScript != null) {
        response.setTypeScript(TypeScriptConvert.INSTANCE.toConvert(typeScript));
      }
    }

    return response;
  }
}
