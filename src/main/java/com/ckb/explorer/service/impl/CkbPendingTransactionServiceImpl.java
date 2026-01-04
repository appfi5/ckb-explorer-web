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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.nervos.ckb.utils.Numeric;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Slf4j
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
    if (ckbPendingTransaction == null) {
      return null;
    }

    // 翻页查询对应的input
    Page<PendingCellInputDto> pageResult = new Page<>(page, pageSize);
    byte[] txHashBytes = Numeric.hexStringToByteArray(txHash);
    Page<PendingCellInputDto> resultPage = pendingInputMapper.getDisplayInputs(pageResult,
        txHashBytes);
    List<PendingCellInputDto> pendingCellInputDtos = resultPage.getRecords();
    if (pendingCellInputDtos.isEmpty()) {
      return new Page<CellInputResponse>(pageResult.getPages(), pageResult.getSize(), 0); // 空列表直接返回
    }
    // 筛选需要补充output数据的DTO（lock_script_hash为null)
    List<PreOutpointTxHashAndIndexDto> preOutpointTxHashAndIndexs = pendingCellInputDtos.stream()
        .filter(cellInputDto -> cellInputDto.getLockScriptHash() == null)
        .map(cellInputDto -> new PreOutpointTxHashAndIndexDto(cellInputDto.getGeneratedTxHash(), cellInputDto.getCellIndex()))
        .collect(Collectors.toList());
    if (preOutpointTxHashAndIndexs.isEmpty()) {
      return CellInputConvert.INSTANCE.toConvertPendingPage(resultPage); // 无需补充数据，直接返回
    }

    // 构建Map快速查找DTO
    Map<String, Map<Integer, PendingCellInputDto>> dtoMap = new HashMap<>();
    for (PendingCellInputDto dto : pendingCellInputDtos) {
      String generatedTxHash = dto.getGeneratedTxHash();
      Integer cellIndex = dto.getCellIndex();
      // 按generatedTxHash分组，内层Map以cellIndex为Key
      dtoMap.computeIfAbsent(generatedTxHash, k -> new HashMap<>())
          .put(cellIndex, dto);
    }

    // 循环补充数据
    for (PreOutpointTxHashAndIndexDto txHashAndIndex : preOutpointTxHashAndIndexs) {
      String generatedTxHash = txHashAndIndex.getGeneratedTxHash();
      Integer cellIndex = txHashAndIndex.getCellIndex();

      Map<Integer, PendingCellInputDto> innerMap = dtoMap.get(generatedTxHash);
      if (innerMap == null) {
        continue; // 无对应DTO，跳过
      }

      PendingCellInputDto pendingCellInputDto = innerMap.get(cellIndex);
      if (pendingCellInputDto == null) {
        continue; // 无对应DTO，跳过
      }

      log.info("tx:{},index:{}对应output是旧数据", generatedTxHash,cellIndex);
      byte[] generatedTxHashBytes = Numeric.hexStringToByteArray(generatedTxHash);
      PendingCellInputDto output = outputMapper.getOutputByTxHashAndIndex(generatedTxHashBytes, cellIndex);
      if (output == null) {
        log.warn("未找到对应输出数据：generatedTxHash={}, cellIndex={}", generatedTxHash, cellIndex);
        continue; // 无输出数据，跳过赋值
      }
      BeanUtils.copyProperties(output, pendingCellInputDto);
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
