package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.dto.CellInputDto;
import com.ckb.explorer.domain.dto.CellOutputDto;
import com.ckb.explorer.domain.dto.TransactionDto;
import com.ckb.explorer.domain.resp.BlockTransactionPageResponse;
import com.ckb.explorer.domain.resp.CellInputResponse;
import com.ckb.explorer.domain.resp.CellOutputResponse;
import com.ckb.explorer.domain.resp.TransactionResponse;
import com.ckb.explorer.entity.AccountBook;
import com.ckb.explorer.entity.Address;
import com.ckb.explorer.entity.CkbTransaction;
import com.ckb.explorer.mapper.AccountBookMapper;
import com.ckb.explorer.mapper.AddressMapper;
import com.ckb.explorer.mapper.CkbTransactionMapper;
import com.ckb.explorer.mapper.InputMapper;
import com.ckb.explorer.mapper.OutputMapper;
import com.ckb.explorer.mapstruct.BlockTransactionConvert;
import com.ckb.explorer.mapstruct.CellInputConvert;
import com.ckb.explorer.mapstruct.CellOutputConvert;
import com.ckb.explorer.mapstruct.CkbTransactionConvert;
import com.ckb.explorer.service.CkbTransactionService;
import com.ckb.explorer.util.I18n;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.nervos.ckb.utils.Numeric;
import org.springframework.beans.factory.annotation.Autowired;
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
  private AddressMapper addressMapper;

  @Resource
  private AccountBookMapper accountBookMapper;

  @Resource
  private CkbTransactionMapper ckbTransactionMapper;

  // 有效的排序字段
  private static final Set<String> VALID_SORT_FIELDS = new HashSet<String>() {
    {
      add("id");
      add("blockNumber");
      add("capacityInvolved");
    }
  };

  @Override
  public Page<CkbTransaction> getCkbTransactionsByPage(int pageNum, int pageSize, String sort) {
    // 解析排序参数
    String[] sortParts = sort.split("\\.", 2);
    String orderBy = sortParts[0];
    String ascOrDesc = sortParts.length > 1 ? sortParts[1].toLowerCase() : "desc";

    // 字段映射
    orderBy = switch (orderBy) {
      case "height" -> "blockNumber";
      case "capacity" -> "capacityInvolved";
      default -> orderBy;
    };

    // 验证排序字段
    if (!VALID_SORT_FIELDS.contains(orderBy)) {
      throw new ServerException(i18n.getMessage(I18nKey.SORT_ERROR_MESSAGE));
    }

    // 创建分页对象
    Page<CkbTransaction> pageResult = new Page<>(pageNum, pageSize);
    // 创建查询条件
    LambdaQueryWrapper<CkbTransaction> queryWrapper = new LambdaQueryWrapper<>();

    // 添加排序条件
    boolean isAsc = "asc".equals(ascOrDesc);
    switch (orderBy) {
      case "id":
        if (isAsc) {
          queryWrapper.orderByAsc(CkbTransaction::getId);
        } else {
          queryWrapper.orderByDesc(CkbTransaction::getId);
        }
        break;
      case "blockNumber":
        if (isAsc) {
          queryWrapper.orderByAsc(CkbTransaction::getBlockNumber);
        } else {
          queryWrapper.orderByDesc(CkbTransaction::getBlockNumber);
        }
        break;
      case "capacityInvolved":
        if (isAsc) {
          queryWrapper.orderByAsc(CkbTransaction::getCapacityInvolved);
        } else {
          queryWrapper.orderByDesc(CkbTransaction::getCapacityInvolved);
        }
        break;
    }

    // 执行分页查询
    return baseMapper.selectPage(pageResult, queryWrapper);
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

      // TODO 如果是资产的交易，则需要拼接额外的信息
//    if previous_cell_output.nervos_dao_withdrawing?
//        display_input.merge!(attributes_for_dao_input(previous_cell_output))
//    end
//    if previous_cell_output.nervos_dao_deposit?
//        display_input.merge!(attributes_for_dao_input(cell_outputs[cell_input.index], false))
//    end
//    if previous_cell_output.udt?
//        display_input.merge!(attributes_for_udt_cell(previous_cell_output))
//    end
//    if previous_cell_output.xudt?
//        display_input.merge!(attributes_for_xudt_cell(previous_cell_output))
//    end
//    if previous_cell_output.ssri?
//        display_input.merge!(attributes_for_ssri_cell(previous_cell_output))
//    end
//    if previous_cell_output.xudt_compatible?
//        display_input.merge!(attributes_for_xudt_compatible_cell(previous_cell_output))
//    end
//    if previous_cell_output.cell_type.in?(%w(m_nft_issuer m_nft_class m_nft_token))
//    display_input.merge!(attributes_for_m_nft_cell(previous_cell_output))
//    end
//    if previous_cell_output.cell_type.in?(%w(nrc_721_token nrc_721_factory))
//    display_input.merge!(attributes_for_nrc_721_cell(previous_cell_output))
//    end
//    if previous_cell_output.cell_type.in?(%w(omiga_inscription_info omiga_inscription))
//    display_input.merge!(attributes_for_omiga_inscription_cell(previous_cell_output))
//    end
//    if previous_cell_output.bitcoin_vout
//    display_input.merge!(attributes_for_rgb_cell(previous_cell_output))
//    end
//    if previous_cell_output.cell_type.in?(%w(spore_cluster spore_cell did_cell))
//    display_input.merge!(attributes_for_dob_cell(previous_cell_output))
//    end
//    if previous_cell_output.lock_script.code_hash == Settings.fiber_funding_code_hash
//    display_input.merge!(attributes_for_fiber_cell(previous_cell_output))
//    end
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

    return CellInputConvert.INSTANCE.toConvertPage(resultPage);
  }

  @Override
  public Page<CellOutputResponse> getDisplayOutputs(String txHash, int pageNum, int pageSize) {
    CkbTransaction ckbTransaction = getCkbTransaction(txHash);
    Page<CellOutputDto> pageResult = new Page<>(pageNum, pageSize);
    // cellbase交易
    if(ckbTransaction.getTxIndex() == 0){

      Page<CellOutputDto> resultPage = outputMapper.getCellbaseDisplayOutputs(pageResult, ckbTransaction.getId());

      // TODO 标签判断：如果区块时间戳等于创世区块时间戳则包含标签，否则为空列表
      Page<CellOutputResponse> result = CellOutputConvert.INSTANCE.toConvertPage(resultPage);
      List<CellOutputResponse> records = result.getRecords();
      records.stream().forEach(record-> record.setTargetBlockNumber(ckbTransaction.getBlockNumber()< 11 ?0:ckbTransaction.getBlockNumber()-11));
      return result;
      // 普通交易
    } else {
      Page<CellOutputDto> resultPage = outputMapper.getNormalTxDisplayOutputs(pageResult, ckbTransaction.getId());

      return CellOutputConvert.INSTANCE.toConvertPage(resultPage);
      //    display_output.merge!(attributes_for_udt_cell(output)) if output.udt?
//        display_output.merge!(attributes_for_xudt_cell(output)) if output.xudt?
//        display_output.merge!(attributes_for_ssri_cell(output)) if output.ssri?
//        display_output.merge!(attributes_for_xudt_compatible_cell(output)) if output.xudt_compatible?
//        display_output.merge!(attributes_for_cota_registry_cell(output)) if output.cota_registry?
//        display_output.merge!(attributes_for_cota_regular_cell(output)) if output.cota_regular?
//    if output.cell_type.in?(%w(m_nft_issuer m_nft_class m_nft_token))
//    display_output.merge!(attributes_for_m_nft_cell(output))
//    end
//    if output.cell_type.in?(%w(nrc_721_token nrc_721_factory))
//    display_output.merge!(attributes_for_nrc_721_cell(output))
//    end
//    if output.cell_type.in?(%w(omiga_inscription_info omiga_inscription))
//    display_output.merge!(attributes_for_omiga_inscription_cell(output))
//    end
//    if output.bitcoin_vout
//    display_output.merge!(attributes_for_rgb_cell(output))
//    end
//    if output.cell_type.in?(%w(spore_cluster spore_cell did_cell))
//    display_output.merge!(attributes_for_dob_cell(output))
//    end
//    if output.lock_script.code_hash == Settings.fiber_funding_code_hash
//    display_output.merge!(attributes_for_fiber_cell(output))
//    end
    }
  }

  private CkbTransaction getCkbTransaction(String txHash) {
    LambdaQueryWrapper<CkbTransaction> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(CkbTransaction::getTxHash, Numeric.hexStringToByteArray(txHash));
    return baseMapper.selectOne(queryWrapper);
  }

  @Override
  public Page<BlockTransactionPageResponse> getBlockTransactions(String blockHash, String txHash,
      String addressHash, int page, int pageSize) {

    // 创建分页对象
    Page<CkbTransaction> transactionPage = new Page<>(page, pageSize);
    LambdaQueryWrapper<CkbTransaction> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(CkbTransaction::getBlockHash, Numeric.hexStringToByteArray(blockHash));

    if (txHash != null && !txHash.isEmpty()) {
      queryWrapper.eq(CkbTransaction::getTxHash, Numeric.hexStringToByteArray(txHash));
    }

    // TODO 如果指定地址
    if (addressHash != null && !addressHash.isEmpty()) {
      // 查找地址
      Address address = addressMapper.selectOne(new LambdaQueryWrapper<Address>().eq(Address::getAddress, Numeric.hexStringToByteArray(addressHash)));
      if (address == null) {
        throw new ServerException(I18nKey.ADDRESS_NOT_FOUND_CODE, i18n.getMessage(I18nKey.ADDRESS_NOT_FOUND_MESSAGE));
      }

      // 查询该地址相关的交易ID
      List<Long> transactionIds = accountBookMapper.selectList(
              new LambdaQueryWrapper<AccountBook>().eq(AccountBook::getAddressId, address.getId()))
          .stream()
          .map(AccountBook::getTransactionId)
          .filter(Objects::nonNull)
          .distinct()
          .toList();

      if (!transactionIds.isEmpty()) {
        queryWrapper.in(CkbTransaction::getId, transactionIds);
      }
    }

    queryWrapper.orderByAsc(CkbTransaction::getTxIndex);

    // 执行分页查询
    Page<CkbTransaction> resultPage = ckbTransactionMapper.selectPage(transactionPage, queryWrapper);

    var result = BlockTransactionConvert.INSTANCE.toConvertPage(resultPage);

    var transactions = result.getRecords();
    var cellbaseTransactionsIds = transactions.stream().filter(transaction-> transaction.getIsCellbase()).map(BlockTransactionPageResponse::getId).collect(Collectors.toList());
    var normalTransactionsIds = transactions.stream().filter(transaction-> !transaction.getIsCellbase()).map(BlockTransactionPageResponse::getId).collect(Collectors.toList());

    Map<Long,List<CellOutputDto>>  cellbaseOutputsWithTrans;
    Map<Long,List<CellInputDto>> normalInputsWithTrans = new HashMap<>();
    Map<Long,List<CellOutputDto>>  normalOutputsWithTrans= new HashMap<>();
    if(cellbaseTransactionsIds.size() > 0){
      cellbaseOutputsWithTrans = outputMapper.getCellbaseDisplayOutputsByTransactionIds(cellbaseTransactionsIds).stream().collect(Collectors.groupingBy(CellOutputDto::getTransactionId));
    } else {
      cellbaseOutputsWithTrans = new HashMap<>();
    }
    if(normalTransactionsIds.size() > 0){
      normalInputsWithTrans = inputMapper.getNormalDisplayInputsByTransactionIds(normalTransactionsIds, pageSize).stream().collect(Collectors.groupingBy(CellInputDto::getTransactionId));
      normalOutputsWithTrans = outputMapper.getNormalTxDisplayOutputsByTransactionIds(normalTransactionsIds, pageSize).stream().collect(Collectors.groupingBy(CellOutputDto::getTransactionId));
    }
    Map<Long, List<CellInputDto>> finalNormalInputsWithTrans = normalInputsWithTrans;
    Map<Long, List<CellOutputDto>> finalNormalOutputsWithTrans = normalOutputsWithTrans;
    transactions.stream().forEach(transaction -> {
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
        cellbaseOutput.stream().forEach(record-> record.setTargetBlockNumber(targetBlockNumber));

        transaction.setDisplayOutputs(cellbaseOutput);
      }else{
        transaction.setDisplayInputs(CellInputConvert.INSTANCE.toConvertList(
            finalNormalInputsWithTrans.get(transaction.getId())));

        transaction.setDisplayOutputs(CellOutputConvert.INSTANCE.INSTANCE.toConvertList(
            finalNormalOutputsWithTrans.get(transaction.getId())));
      }
    });

    return result;
  }


}
