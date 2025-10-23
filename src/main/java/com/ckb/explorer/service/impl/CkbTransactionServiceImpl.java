package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.config.ScriptConfig;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.dto.CellInputDto;
import com.ckb.explorer.domain.dto.CellOutputDto;
import com.ckb.explorer.domain.dto.TransactionDto;
import com.ckb.explorer.domain.req.ContractTransactionsPageReq;
import com.ckb.explorer.domain.req.UdtTransactionsPageReq;
import com.ckb.explorer.domain.resp.*;
import com.ckb.explorer.entity.CkbTransaction;
import com.ckb.explorer.entity.Script;
import com.ckb.explorer.mapper.Address24hTransactionMapper;
import com.ckb.explorer.mapper.CkbTransactionMapper;
import com.ckb.explorer.mapper.DaoContractMapper;
import com.ckb.explorer.mapper.DepositCellMapper;
import com.ckb.explorer.mapper.InputMapper;
import com.ckb.explorer.mapper.OutputMapper;
import com.ckb.explorer.mapper.ScriptMapper;
import com.ckb.explorer.mapstruct.BlockTransactionConvert;
import com.ckb.explorer.mapstruct.CellInputConvert;
import com.ckb.explorer.mapstruct.CellOutputConvert;
import com.ckb.explorer.mapstruct.CkbTransactionConvert;
import com.ckb.explorer.service.CkbTransactionService;
import com.ckb.explorer.service.ScriptService;
import com.ckb.explorer.util.I18n;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;
import org.springframework.stereotype.Service;

@Service
public class CkbTransactionServiceImpl extends ServiceImpl<CkbTransactionMapper, CkbTransaction> implements
    CkbTransactionService {

  @Resource
  private I18n i18n;

  @Resource
  private InputMapper inputMapper;

  @Resource
  private OutputMapper outputMapper;

  @Resource
  private CkbTransactionMapper ckbTransactionMapper;

  @Resource
  private Address24hTransactionMapper address24hTransactionMapper;

  @Resource
  private ScriptMapper scriptMapper;

  @Resource
  ScriptService scriptService;

  @Resource
  private DaoContractMapper daoContractMapper;

  @Resource
  private DepositCellMapper depositCellMapper;

  @Resource
  ScriptConfig scriptConfig;

  @Override
  public List<TransactionPageResponse> getHomePageTransactions(int size) {

    return baseMapper.getHomePageTransactions( size);

  }

  @Override
  public Page<TransactionPageResponse> getCkbTransactionsByPage(int pageNum, int pageSize, String sort) {

    // 创建分页对象
    Page<CkbTransaction> pageResult = new Page<>(pageNum, pageSize);

    // 执行分页查询
    return baseMapper.getPageTransactions(pageResult);
  }

  @Override
  public TransactionResponse getTransactionByHash(String txHash) {

    // 查询第一个匹配的交易
    TransactionDto transaction = baseMapper.selectTransactionWithCellDeps(Numeric.hexStringToByteArray(txHash));
    
    // 如果找不到交易，抛出异常
    if (transaction == null) {
      throw new ServerException(I18nKey.CKB_TRANSACTION_NOT_FOUND_CODE, i18n.getMessage(I18nKey.CKB_TRANSACTION_NOT_FOUND_MESSAGE));
    }

    TransactionResponse result = CkbTransactionConvert.INSTANCE.toConvertTransactionResponse(transaction);

    return result;
  }

  @Override
  public Page<CellInputResponse> getDisplayInputs(String txHash, int pageNum, int pageSize) {
    CkbTransaction ckbTransaction = getCkbTransaction(txHash);
    // cellbase交易
    if(ckbTransaction.getTxIndex() == 0){

      return getCellbaseDisplayInputs(txHash, ckbTransaction.getBlockNumber());
      // 普通交易
    } else{
      return getNormalTxDisplayInputs(ckbTransaction.getId(), pageNum, pageSize);
    }
  }

  /**
   * 处理cellbase交易的DisplayInputs
   * @param txHash
   * @param blockNumber
   * @return
   */
  private Page<CellInputResponse> getCellbaseDisplayInputs(String txHash,Long blockNumber) {
    Page<CellInputResponse> page = new Page<>(1, 1);
    List<CellInputResponse> records = new ArrayList<>();
    
    // Cellbase交易只有一个特殊的输入
    CellInputResponse cellInput = new CellInputResponse();
    cellInput.setFromCellbase(true);
    cellInput.setTargetBlockNumber(blockNumber< 11 ?0:blockNumber-11);
    cellInput.setGeneratedTxHash(txHash);
    
    records.add(cellInput);
    page.setRecords(records);
    page.setTotal(1);
    
    return page;
  }


  /**
   * 处理普通交易的DisplayInputs
   * @param transactionId
   * @param page
   * @param pageSize
   * @return
   */
  private Page<CellInputResponse> getNormalTxDisplayInputs(Long transactionId, int page, int pageSize) {
    Page<CellInputDto> pageResult = new Page<>(page, pageSize);
    Page<CellInputDto> resultPage = inputMapper.getDisplayInputs(pageResult, transactionId);
    resultPage.getRecords().forEach(cellInputDto -> {
      ScriptConfig.TypeScript typeScript = scriptConfig.getTypeScriptByCodeHash(cellInputDto.getCodeHash(),cellInputDto.getArgs());
      Integer cellType = scriptConfig.cellType(typeScript,Numeric.toHexString(cellInputDto.getData()));
      cellInputDto.setCellType(cellType);
    });
    return CellInputConvert.INSTANCE.toConvertPage(resultPage);
  }

  @Override
  public Page<CellOutputResponse> getDisplayOutputs(String txHash, int pageNum, int pageSize) {
    CkbTransaction ckbTransaction = getCkbTransaction(txHash);
    Page<CellOutputDto> pageResult = new Page<>(pageNum, pageSize);
    // cellbase交易
    if(ckbTransaction.getTxIndex() == 0){

      Page<CellOutputDto> resultPage = outputMapper.getCellbaseDisplayOutputs(pageResult, ckbTransaction.getId());
      resultPage.getRecords().forEach(cellOutputDto -> {
        if(StringUtils.isNotEmpty(cellOutputDto.getCodeHash())){
          ScriptConfig.TypeScript typeScript = scriptConfig.getTypeScriptByCodeHash(cellOutputDto.getCodeHash(),cellOutputDto.getArgs());
          Integer cellType = scriptConfig.cellType(typeScript,Numeric.toHexString(cellOutputDto.getData()));
          cellOutputDto.setCellType(cellType);
        }

      });

      Page<CellOutputResponse> result = CellOutputConvert.INSTANCE.toConvertPage(resultPage);
      List<CellOutputResponse> records = result.getRecords();
      records.stream().forEach(record-> record.setTargetBlockNumber(ckbTransaction.getBlockNumber()< 11 ?0:ckbTransaction.getBlockNumber()-11));
      return result;
      // 普通交易
    } else {
      Page<CellOutputDto> resultPage = outputMapper.getNormalTxDisplayOutputs(pageResult, ckbTransaction.getId());
      resultPage.getRecords().forEach(cellOutputDto -> {
        if(StringUtils.isNotEmpty(cellOutputDto.getCodeHash())) {
          ScriptConfig.TypeScript typeScript = scriptConfig.getTypeScriptByCodeHash(cellOutputDto.getCodeHash(), cellOutputDto.getArgs());
          Integer cellType = scriptConfig.cellType(typeScript, Numeric.toHexString(cellOutputDto.getData()));
          cellOutputDto.setCellType(cellType);
        }
      });
      return CellOutputConvert.INSTANCE.toConvertPage(resultPage);
    }
  }

  private CkbTransaction getCkbTransaction(String txHash) {
    LambdaQueryWrapper<CkbTransaction> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(CkbTransaction::getTxHash, Numeric.hexStringToByteArray(txHash));
    return baseMapper.selectOne(queryWrapper);
  }

  private Script getAddress(String addressHash) {
    // 计算地址的哈希
    var addressScriptHash = Address.decode(addressHash).getScript().computeHash();
    LambdaQueryWrapper<Script> queryScriptWrapper = new LambdaQueryWrapper<>();
    queryScriptWrapper.eq(Script::getScriptHash, addressScriptHash);
    return scriptMapper.selectOne(queryScriptWrapper);
  }

  @Override
  public Page<BlockTransactionPageResponse> getBlockTransactions(String blockHash, String txHash,
      String addressHash, int page, int pageSize) {

    // 创建分页对象
    Page<CkbTransaction> transactionPage = new Page<>(page, pageSize);

    Long lockScriptId = null;
    // 如果指定地址
    if (addressHash != null && !addressHash.isEmpty()) {
      // 查找地址
      var script = getAddress(addressHash);
      if(script == null){
        throw new ServerException(I18nKey.ADDRESS_NOT_FOUND_CODE, i18n.getMessage(I18nKey.ADDRESS_NOT_FOUND_MESSAGE));
      }
      lockScriptId = script.getId();
    }

    // 执行分页查询
    Page<CkbTransaction> resultPage = ckbTransactionMapper.selectPageByBlockHash(transactionPage, Numeric.hexStringToByteArray(blockHash), txHash != null && !txHash.isEmpty() ?Numeric.hexStringToByteArray(txHash): null, lockScriptId);

    var result = BlockTransactionConvert.INSTANCE.toConvertPage(resultPage);

    var transactions = result.getRecords();
    // 分开处理cellbase和普通交易
    var cellbaseTransactionsIds = transactions.stream().filter(transaction-> transaction.getIsCellbase()).map(BlockTransactionPageResponse::getId).collect(Collectors.toList());
    var normalTransactionsIds = transactions.stream().filter(transaction-> !transaction.getIsCellbase()).map(BlockTransactionPageResponse::getId).collect(Collectors.toList());

    Map<Long,List<CellOutputDto>>  cellbaseOutputsWithTrans;
    Map<Long,List<CellInputDto>> normalInputsWithTrans ;
    Map<Long,List<CellOutputDto>>  normalOutputsWithTrans;

    // cellbase获取output
    if(cellbaseTransactionsIds.size() > 0){
      cellbaseOutputsWithTrans = outputMapper.getCellbaseDisplayOutputsByTransactionIds(cellbaseTransactionsIds).stream().collect(Collectors.groupingBy(CellOutputDto::getTransactionId));
    } else {
      cellbaseOutputsWithTrans = new HashMap<>();
    }

    // 普通交易获取input和output
    if(normalTransactionsIds.size() > 0){
      List<CellInputDto> normalInputs = inputMapper.getNormalDisplayInputsByTransactionIds(normalTransactionsIds, pageSize);
      normalInputs.forEach(cellInputDto -> {
        if(StringUtils.isNotEmpty(cellInputDto.getCodeHash())) {
          ScriptConfig.TypeScript typeScript = scriptConfig.getTypeScriptByCodeHash(cellInputDto.getCodeHash(), cellInputDto.getArgs());
          Integer cellType = scriptConfig.cellType(typeScript, Numeric.toHexString(cellInputDto.getData()));
          cellInputDto.setCellType(cellType);
        }
      });
      normalInputsWithTrans = normalInputs.stream().collect(Collectors.groupingBy(CellInputDto::getTransactionId));
      List<CellOutputDto> normalOutputs = outputMapper.getNormalTxDisplayOutputsByTransactionIds(normalTransactionsIds, pageSize);
      normalOutputs.forEach(cellOutputDto -> {
        if(StringUtils.isNotEmpty(cellOutputDto.getCodeHash())) {
          ScriptConfig.TypeScript typeScript = scriptConfig.getTypeScriptByCodeHash(cellOutputDto.getCodeHash(), cellOutputDto.getArgs());
          Integer cellType = scriptConfig.cellType(typeScript, Numeric.toHexString(cellOutputDto.getData()));
          cellOutputDto.setCellType(cellType);
        }
      });
      normalOutputsWithTrans = normalOutputs.stream().collect(Collectors.groupingBy(CellOutputDto::getTransactionId));
    } else{
      normalInputsWithTrans = new HashMap<>();
      normalOutputsWithTrans = new HashMap<>();
    }

    transactions.stream().forEach(transaction -> {
      // 组装cellbase交易
      if(transaction.getIsCellbase()){
        Long targetBlockNumber = transaction.getBlockNumber()< 11 ?0:transaction.getBlockNumber()-11;
        List<CellInputResponse> records = new ArrayList<>();

        // Cellbase交易只有一个特殊的输入
        CellInputResponse cellInput = new CellInputResponse();
        cellInput.setFromCellbase(true);
        cellInput.setTargetBlockNumber(targetBlockNumber);
        cellInput.setGeneratedTxHash(transaction.getTransactionHash());

        records.add(cellInput);
        transaction.setDisplayInputs(records);

        List<CellOutputResponse> cellbaseOutput = CellOutputConvert.INSTANCE.INSTANCE.toConvertList(cellbaseOutputsWithTrans.get(transaction.getId()));
        if(cellbaseOutput != null && cellbaseOutput.size() > 0){
          cellbaseOutput.stream().forEach(record-> record.setTargetBlockNumber(targetBlockNumber));
        } else{
          cellbaseOutput = new ArrayList<>();
        }


        transaction.setDisplayOutputs(cellbaseOutput);
        // 组装普通交易
      }else{
        transaction.setDisplayInputs(CellInputConvert.INSTANCE.toConvertList(
            normalInputsWithTrans.get(transaction.getId())));

        transaction.setDisplayOutputs(CellOutputConvert.INSTANCE.INSTANCE.toConvertList(
            normalOutputsWithTrans.get(transaction.getId())));
      }
    });

    return result;
  }


  @Override
  public Page<AddressTransactionPageResponse> getAddressTransactions(String address, String sort,
      int page, int pageSize) {

    String[] sortParts = sort.split("\\.", 2);
    String orderBy = sortParts[0];
    String ascOrDesc = sortParts.length > 1 ? sortParts[1].toLowerCase() : "desc";

    orderBy = switch (orderBy) {
      case "time" -> "block_timestamp";
      default -> throw new ServerException(i18n.getMessage(I18nKey.SORT_ERROR_MESSAGE));
    };

    // 判断地址是否存在
    LambdaQueryWrapper<Script> queryScriptWrapper = new LambdaQueryWrapper<>();
    var addressScriptHash = Address.decode(address).getScript().computeHash();
    queryScriptWrapper.eq(Script::getScriptHash, addressScriptHash);
    var script = scriptMapper.selectOne(queryScriptWrapper);
    if (script == null) {
      throw new ServerException(I18nKey.ADDRESS_NOT_FOUND_CODE,
          i18n.getMessage(I18nKey.ADDRESS_NOT_FOUND_MESSAGE));
    }

    // 从24小时表里获取翻页的交易id
    Page<Long> transactionIdsPage = new Page<>(page, pageSize);
    Page<Long> transactionIdPage = address24hTransactionMapper.getTransactionsLast24hrsByLockScriptIdWithSort(
        transactionIdsPage, script.getId(), orderBy, ascOrDesc);

    List<Long> transactionIds = transactionIdPage.getRecords();
    if (transactionIds.isEmpty()){
      return Page.of(page, pageSize, 0);
    }

    // 根据交易id查询交易详情
    List<AddressTransactionPageResponse> transactions = baseMapper.selectByTransactionIds(transactionIds, orderBy, ascOrDesc);

    // 分开处理cellbase和普通交易
    var cellbaseTransactionsIds = transactions.stream().filter(transaction-> transaction.getIsCellbase()).map(AddressTransactionPageResponse::getId).collect(Collectors.toList());
    var normalTransactionsIds = transactions.stream().filter(transaction-> !transaction.getIsCellbase()).map(AddressTransactionPageResponse::getId).collect(Collectors.toList());

    Map<Long,List<CellOutputDto>>  cellbaseOutputsWithTrans;
    Map<Long,List<CellInputDto>> normalInputsWithTrans ;
    Map<Long,List<CellOutputDto>>  normalOutputsWithTrans;
    // cellbase获取output
    if(cellbaseTransactionsIds.size() > 0){
      cellbaseOutputsWithTrans = outputMapper.getCellbaseDisplayOutputsByTransactionIds(cellbaseTransactionsIds).stream().collect(Collectors.groupingBy(CellOutputDto::getTransactionId));
    } else{
      cellbaseOutputsWithTrans = new HashMap<>();
    }

    // 普通交易获取input和output
    if(normalTransactionsIds.size() > 0){
      List<CellInputDto> normalInputs = inputMapper.getNormalDisplayInputsByTransactionIds(normalTransactionsIds, pageSize);
      normalInputs.forEach(cellInputDto -> {
        if(StringUtils.isNotEmpty(cellInputDto.getCodeHash())) {
          ScriptConfig.TypeScript typeScript = scriptConfig.getTypeScriptByCodeHash(cellInputDto.getCodeHash(), cellInputDto.getArgs());
          Integer cellType = scriptConfig.cellType(typeScript, Numeric.toHexString(cellInputDto.getData()));
          cellInputDto.setCellType(cellType);
        }
      });
      normalInputsWithTrans = normalInputs.stream().collect(Collectors.groupingBy(CellInputDto::getTransactionId));
      List<CellOutputDto> normalOutputs = outputMapper.getNormalTxDisplayOutputsByTransactionIds(normalTransactionsIds, pageSize);
      normalOutputs.forEach(cellOutputDto -> {
        if(StringUtils.isNotEmpty(cellOutputDto.getCodeHash())) {
          ScriptConfig.TypeScript typeScript = scriptConfig.getTypeScriptByCodeHash(cellOutputDto.getCodeHash(), cellOutputDto.getArgs());
          Integer cellType = scriptConfig.cellType(typeScript, Numeric.toHexString(cellOutputDto.getData()));
          cellOutputDto.setCellType(cellType);
        }
      });
      normalOutputsWithTrans = normalOutputs.stream().collect(Collectors.groupingBy(CellOutputDto::getTransactionId));
    } else{
      normalInputsWithTrans = new HashMap<>();
      normalOutputsWithTrans = new HashMap<>();
    }

    transactions.stream().forEach(transaction -> {
      // 组装cellbase交易
      if(transaction.getIsCellbase()){
        Long targetBlockNumber = transaction.getBlockNumber()< 11 ?0:transaction.getBlockNumber()-11;
        List<CellInputResponse> records = new ArrayList<>();

        // Cellbase交易只有一个特殊的输入
        CellInputResponse cellInput = new CellInputResponse();
        cellInput.setFromCellbase(true);
        cellInput.setTargetBlockNumber(targetBlockNumber);
        cellInput.setGeneratedTxHash(transaction.getTransactionHash());

        records.add(cellInput);
        transaction.setDisplayInputs(records);

        List<CellOutputResponse> cellbaseOutput = CellOutputConvert.INSTANCE.INSTANCE.toConvertList(cellbaseOutputsWithTrans.get(transaction.getId()));
        if(cellbaseOutput != null && cellbaseOutput.size() > 0){
          cellbaseOutput.stream().forEach(record-> record.setTargetBlockNumber(targetBlockNumber));
        }else{
          cellbaseOutput = new ArrayList<>();
        }

        transaction.setDisplayOutputs(cellbaseOutput);
        // 组装普通交易
      }else{
        transaction.setDisplayInputs(CellInputConvert.INSTANCE.toConvertList(
            normalInputsWithTrans.get(transaction.getId())));

        transaction.setDisplayOutputs(CellOutputConvert.INSTANCE.INSTANCE.toConvertList(
            normalOutputsWithTrans.get(transaction.getId())));
      }
    });

    var result = new Page<AddressTransactionPageResponse>(transactionIdPage.getCurrent(), transactionIdPage.getSize(), transactionIdPage.getTotal());
    result.setRecords(transactions);
    return result;
  }

  @Override
  public Page<UdtTransactionPageResponse> getUdtTransactions(String typeScriptHash, UdtTransactionsPageReq req) {
    Integer page = req.getPage();
    Integer pageSize = req.getPageSize();
    String[] sortParts = req.getSort().split("\\.", 2);
    String orderBy = sortParts[0];
    String ascOrDesc = sortParts.length > 1 ? sortParts[1].toLowerCase() : "desc";

    orderBy = switch (orderBy) {
      case "time" -> "block_timestamp";
      default -> throw new ServerException(i18n.getMessage(I18nKey.SORT_ERROR_MESSAGE));
    };

    // 判断Udt是否存在
    Script script = scriptService.findByScriptHash(typeScriptHash);
    if (script == null) {
      throw new ServerException(I18nKey.UDTS_NOT_FOUND_CODE, i18n.getMessage(I18nKey.UDTS_NOT_FOUND_MESSAGE));
    }

    Long lockScriptId = null;
    if (StringUtils.isNotEmpty(req.getAddressHash())) {
      // 查找地址
      var lockScript = getAddress(req.getAddressHash());
      if(lockScript == null){
        throw new ServerException(I18nKey.ADDRESS_NOT_FOUND_CODE, i18n.getMessage(I18nKey.ADDRESS_NOT_FOUND_MESSAGE));
      }
      lockScriptId = lockScript.getId();
    }

    Long txId = null;
    if(StringUtils.isNotEmpty(req.getTxHash())){
      var ckbTransaction = getCkbTransaction(req.getTxHash());
      if(ckbTransaction==null){
        throw new ServerException(I18nKey.CKB_TRANSACTION_NOT_FOUND_CODE, i18n.getMessage(I18nKey.CKB_TRANSACTION_NOT_FOUND_MESSAGE));
      }
      txId = ckbTransaction.getId();
    }

    // 从24小时表里获取翻页的交易id
    Page<Long> transactionIdsPage = new Page<>(page, pageSize);
    Page<Long> transactionIdPage = address24hTransactionMapper.getTransactionsLast24hrsByTypeScriptIdWithSort(
            transactionIdsPage, script.getId(), orderBy, ascOrDesc,txId,lockScriptId);

    List<Long> transactionIds = transactionIdPage.getRecords();
    if (transactionIds.isEmpty()){
      return Page.of(page, pageSize, 0);
    }

    // 根据交易id查询交易详情
    List<UdtTransactionPageResponse> transactions = CkbTransactionConvert.INSTANCE.toConvertUdtTransactionList(baseMapper.selectByTransactionIds(transactionIds, orderBy, ascOrDesc));

    // 分开处理cellbase和普通交易
    var cellbaseTransactionsIds = transactions.stream().filter(transaction-> transaction.getIsCellbase()).map(UdtTransactionPageResponse::getId).collect(Collectors.toList());
    var normalTransactionsIds = transactions.stream().filter(transaction-> !transaction.getIsCellbase()).map(UdtTransactionPageResponse::getId).collect(Collectors.toList());

    Map<Long,List<CellOutputDto>>  cellbaseOutputsWithTrans;
    Map<Long,List<CellInputDto>> normalInputsWithTrans ;
    Map<Long,List<CellOutputDto>>  normalOutputsWithTrans;
    // cellbase获取output
    if(cellbaseTransactionsIds.size() > 0){
      cellbaseOutputsWithTrans = outputMapper.getCellbaseDisplayOutputsByTransactionIds(cellbaseTransactionsIds).stream().collect(Collectors.groupingBy(CellOutputDto::getTransactionId));
    } else{
      cellbaseOutputsWithTrans = new HashMap<>();
    }

    // 普通交易获取input和output
    if(normalTransactionsIds.size() > 0){
      List<CellInputDto> normalInputs = inputMapper.getNormalDisplayInputsByTransactionIds(normalTransactionsIds, pageSize);
      normalInputs.forEach(cellInputDto -> {
        if(StringUtils.isNotEmpty(cellInputDto.getCodeHash())) {
          ScriptConfig.TypeScript typeScript = scriptConfig.getTypeScriptByCodeHash(cellInputDto.getCodeHash(), cellInputDto.getArgs());
          Integer cellType = scriptConfig.cellType(typeScript, Numeric.toHexString(cellInputDto.getData()));
          cellInputDto.setCellType(cellType);
        }
      });
      normalInputsWithTrans = normalInputs.stream().collect(Collectors.groupingBy(CellInputDto::getTransactionId));
      List<CellOutputDto> normalOutputs = outputMapper.getNormalTxDisplayOutputsByTransactionIds(normalTransactionsIds, pageSize);
      normalOutputs.forEach(cellOutputDto -> {
        if(StringUtils.isNotEmpty(cellOutputDto.getCodeHash())) {
          ScriptConfig.TypeScript typeScript = scriptConfig.getTypeScriptByCodeHash(cellOutputDto.getCodeHash(), cellOutputDto.getArgs());
          Integer cellType = scriptConfig.cellType(typeScript, Numeric.toHexString(cellOutputDto.getData()));
          cellOutputDto.setCellType(cellType);
        }
      });
      normalOutputsWithTrans = normalOutputs.stream().collect(Collectors.groupingBy(CellOutputDto::getTransactionId));
    } else{
      normalInputsWithTrans = new HashMap<>();
      normalOutputsWithTrans = new HashMap<>();
    }

    transactions.stream().forEach(transaction -> {
      // 组装cellbase交易
      if(transaction.getIsCellbase()){
        Long targetBlockNumber = transaction.getBlockNumber()< 11 ?0:transaction.getBlockNumber()-11;
        List<CellInputResponse> records = new ArrayList<>();

        // Cellbase交易只有一个特殊的输入
        CellInputResponse cellInput = new CellInputResponse();
        cellInput.setFromCellbase(true);
        cellInput.setTargetBlockNumber(targetBlockNumber);
        cellInput.setGeneratedTxHash(transaction.getTransactionHash());

        records.add(cellInput);
        transaction.setDisplayInputs(records);

        List<CellOutputResponse> cellbaseOutput = CellOutputConvert.INSTANCE.INSTANCE.toConvertList(cellbaseOutputsWithTrans.get(transaction.getId()));
        if(cellbaseOutput != null && cellbaseOutput.size() > 0){
          cellbaseOutput.stream().forEach(record-> record.setTargetBlockNumber(targetBlockNumber));
        }else{
          cellbaseOutput = new ArrayList<>();
        }

        transaction.setDisplayOutputs(cellbaseOutput);
        // 组装普通交易
      }else{
        transaction.setDisplayInputs(CellInputConvert.INSTANCE.toConvertList(
                normalInputsWithTrans.get(transaction.getId())));

        transaction.setDisplayOutputs(CellOutputConvert.INSTANCE.INSTANCE.toConvertList(
                normalOutputsWithTrans.get(transaction.getId())));
      }
    });

    var result = new Page<UdtTransactionPageResponse>(transactionIdPage.getCurrent(), transactionIdPage.getSize(), transactionIdPage.getTotal());
    result.setRecords(transactions);
    return result;
  }

  @Override
  public Page<ContractTransactionPageResponse> getContractTransactions(ContractTransactionsPageReq req) {

    Integer page = req.getPage();
    Integer pageSize = req.getPageSize();
    Page<ContractTransactionPageResponse> resultPage = new Page<>(page, pageSize);
    // 查DaoContract
    var daoContract = daoContractMapper.defaultContract();
    if(daoContract == null){
      return resultPage.setTotal(0);
    }

    // 处理txHash参数
    byte[] txHash = null;
    if (StringUtils.isNotEmpty(req.getTxHash())) {
      var ckbTransaction = getCkbTransaction(req.getTxHash());
      if (ckbTransaction == null) {
        throw new ServerException(I18nKey.CKB_TRANSACTION_NOT_FOUND_CODE, i18n.getMessage(I18nKey.CKB_TRANSACTION_NOT_FOUND_MESSAGE));
      }
      txHash = Numeric.hexStringToByteArray(req.getTxHash());
    }

    // 处理addressHash参数
    Long lockScriptId = null;
    if (StringUtils.isNotEmpty(req.getAddressHash())) {
      var script = getAddress(req.getAddressHash());
      if (script == null) {
        throw new ServerException(I18nKey.ADDRESS_NOT_FOUND_CODE, i18n.getMessage(I18nKey.ADDRESS_NOT_FOUND_MESSAGE));
      }
      lockScriptId = script.getId();
    }

    Page txHashPage = new Page<>(page, pageSize);
    // 从数据库查询合约交易 从Cell里查
    txHashPage = depositCellMapper.getTxHashPage(txHashPage, txHash, lockScriptId);

    List<byte[]> txHashs = txHashPage.getRecords();
    if(txHashs.isEmpty()){
      return resultPage.setTotal(0);
    }

    // 查具体交易信息
    List<ContractTransactionPageResponse> transactions = baseMapper.selectContractTransactions(txHashs);
    if (transactions.isEmpty()) {
      return resultPage.setTotal(0);
    }

    // 分开处理cellbase和普通交易
    var normalTransactionsIds = transactions.stream().map(ContractTransactionPageResponse::getId).collect(Collectors.toList());

    Map<Long,List<CellInputDto>> normalInputsWithTrans ;
    Map<Long,List<CellOutputDto>>  normalOutputsWithTrans;

    // 普通交易获取input和output
    if(normalTransactionsIds.size() > 0){
      normalInputsWithTrans = inputMapper.getDaoDisplayInputsByTransactionIds(normalTransactionsIds, pageSize).stream().collect(Collectors.groupingBy(CellInputDto::getTransactionId));
      normalOutputsWithTrans = outputMapper.getDaoDisplayOutputsByTransactionIds(normalTransactionsIds, pageSize).stream().collect(Collectors.groupingBy(CellOutputDto::getTransactionId));
    } else {
      normalInputsWithTrans = new HashMap<>();
      normalOutputsWithTrans = new HashMap<>();
    }

    transactions.stream().forEach(transaction -> {

      // 组装交易
      List<CellInputResponse> inputs = CellInputConvert.INSTANCE.toConvertList(normalInputsWithTrans.get(transaction.getId()));
      transaction.setDisplayInputs(inputs);
      transaction.setDisplayInputsCount(inputs != null ? inputs.size() : 0);

      List<CellOutputResponse> outputs = CellOutputConvert.INSTANCE.INSTANCE.toConvertList(normalOutputsWithTrans.get(transaction.getId()));
      transaction.setDisplayOutputs(outputs);
      transaction.setDisplayOutputsCount(outputs != null ? outputs.size() : 0);

    });

    resultPage.setRecords( transactions);
    resultPage.setTotal(txHashPage.getTotal());
    return resultPage;
  }
}
