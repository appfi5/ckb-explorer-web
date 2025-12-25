package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.domain.dto.PendingCellInputDto;
import com.ckb.explorer.domain.dto.PendingCellOutputDto;
import com.ckb.explorer.domain.dto.PendingTransactionDto;
import com.ckb.explorer.domain.dto.PreOutpointTxHashAndIndexDto;
import com.ckb.explorer.domain.resp.CellInputResponse;
import com.ckb.explorer.domain.resp.CellOutputResponse;
import com.ckb.explorer.domain.resp.PendingTransactionPageResponse;
import com.ckb.explorer.domain.resp.TransactionResponse;
import com.ckb.explorer.entity.CkbPendingTransaction;
import com.ckb.explorer.enums.TxStatus;
import com.ckb.explorer.mapper.CkbPendingTransactionMapper;
import com.ckb.explorer.mapper.OutputMapper;
import com.ckb.explorer.mapper.PendingInputMapper;
import com.ckb.explorer.mapper.PendingOutputMapper;
import com.ckb.explorer.mapstruct.CellInputConvert;
import com.ckb.explorer.mapstruct.CellOutputConvert;
import com.ckb.explorer.mapstruct.CkbTransactionConvert;
import com.ckb.explorer.service.CkbPendingTransactionService;
import jakarta.annotation.Resource;
import java.util.List;
import org.nervos.ckb.utils.Numeric;
import org.springframework.stereotype.Service;

@Service
public class CkbPendingTransactionServiceImpl extends
    ServiceImpl<CkbPendingTransactionMapper, CkbPendingTransaction> implements
    CkbPendingTransactionService {

  @Resource
  private PendingInputMapper pendingInputMapper;

  @Resource
  private PendingOutputMapper pendingOutputMapper;

  @Resource
  private OutputMapper outputMapper;

  @Override
  public Page<PendingTransactionPageResponse> getCkbPendingTransactionsByPage(int pageNum,
      int pageSize) {

    // 创建分页对象
    Page<PendingTransactionPageResponse> pageResult = new Page<>(pageNum, pageSize);

    // 执行分页查询
    return baseMapper.getPagePendingTransactions(pageResult);
  }

  @Override
  public TransactionResponse getPendingTransactionByHash(String txHash) {

    // 先查pending表status为 pending的
    PendingTransactionDto pendingTransaction = baseMapper.selectPendingTransactionWithCellDeps(
        Numeric.hexStringToByteArray(txHash));
    if (pendingTransaction == null) {
      return null;
    }
    return CkbTransactionConvert.INSTANCE.toConvertPendingTransactionResponse(pendingTransaction);
  }

  private CkbPendingTransaction getCkbPendingTransaction(String txHash) {
    LambdaQueryWrapper<CkbPendingTransaction> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(CkbPendingTransaction::getTxHash, Numeric.hexStringToByteArray(txHash));
    queryWrapper.eq(CkbPendingTransaction::getStatus, TxStatus.pending.getCode());
    return baseMapper.selectOne(queryWrapper);
  }

  @Override
  public Page<CellInputResponse> getDisplayInputs(String txHash, int page, int pageSize) {
    // 先查pending表status为 pending的，如果有数据，则查pending库的input找到对应的pre_outpoint_tx_hash+pre_outpoint_index
    // 根据pre_outpoint_tx_hash+pre_outpoint_index查pending库的output，如果存在则返回，不存在则查原库的output
    CkbPendingTransaction ckbPendingTransaction = getCkbPendingTransaction(txHash);
    if(ckbPendingTransaction == null){
      return null;
    }

    Page pageResult = new Page<>(page, pageSize);
    Page<PendingCellInputDto> resultPage = pendingInputMapper.getDisplayInputs(pageResult, Numeric.hexStringToByteArray(txHash));
    List<PendingCellInputDto> pendingCellInputDtos = resultPage.getRecords();
    // 筛选出lock_script_hash为null的generatedTxHash和cellIndex
    List<PreOutpointTxHashAndIndexDto> preOutpointTxHashAndIndexs = pendingCellInputDtos.stream().filter(cellInputDto -> cellInputDto.getLockScriptHash() == null).map(cellInputDto -> new PreOutpointTxHashAndIndexDto(cellInputDto.getGeneratedTxHash(),cellInputDto.getCellIndex())).toList();
    if(!preOutpointTxHashAndIndexs.isEmpty()){
      for(PreOutpointTxHashAndIndexDto txHashAndIndex: preOutpointTxHashAndIndexs){
        PendingCellInputDto pendingCellInputDto = pendingCellInputDtos.stream().filter(cellInputDto -> cellInputDto.getGeneratedTxHash().equals(txHashAndIndex.getGeneratedTxHash()) && cellInputDto.getCellIndex().equals(txHashAndIndex.getCellIndex())).findFirst().orElse(null);
        if(pendingCellInputDto != null){
          PendingCellInputDto output = outputMapper.getOutputByTxHashAndIndex(txHashAndIndex.getGeneratedTxHash(),txHashAndIndex.getCellIndex());
          pendingCellInputDto.setId(output.getId());
          pendingCellInputDto.setCapacity(output.getCapacity());
          pendingCellInputDto.setOccupiedCapacity(output.getOccupiedCapacity());
          pendingCellInputDto.setLockCodeHash(output.getTypeCodeHash());
          pendingCellInputDto.setLockHashType(output.getTypeHashType());
          pendingCellInputDto.setLockArgs(output.getTypeArgs());
          pendingCellInputDto.setLockScriptHash(output.getLockScriptHash());
          pendingCellInputDto.setTypeCodeHash(output.getTypeCodeHash());
          pendingCellInputDto.setTypeHashType(output.getTypeHashType());
          pendingCellInputDto.setTypeArgs(output.getTypeArgs());
          pendingCellInputDto.setTypeScriptHash(output.getTypeScriptHash());
          pendingCellInputDto.setData(output.getData());
        }
      }
    }

    return CellInputConvert.INSTANCE.toConvertPendingPage(resultPage);
  }

  @Override
  public Page<CellOutputResponse> getDisplayOutputs(String txHash, int page, int pageSize) {
    // 先查pending表status为 pending的，如果有数据，则查pending库output，如果存在则返回，不存在则返回空
    CkbPendingTransaction ckbPendingTransaction = getCkbPendingTransaction(txHash);
    if(ckbPendingTransaction == null){
      return null;
    }

    Page pageResult = new Page<>(page, pageSize);
    Page<PendingCellOutputDto> resultPage = pendingOutputMapper.getDisplayOutputs(pageResult, Numeric.hexStringToByteArray(txHash));

    return CellOutputConvert.INSTANCE.toConvertPendingPage(resultPage);
  }
}
