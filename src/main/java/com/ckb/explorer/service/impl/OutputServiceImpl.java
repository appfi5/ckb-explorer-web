package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.resp.AddressLiveCellsResponse;
import com.ckb.explorer.entity.Output;
import com.ckb.explorer.entity.Script;
import com.ckb.explorer.mapper.OutputMapper;
import com.ckb.explorer.mapper.ScriptMapper;
import com.ckb.explorer.service.OutputService;
import com.ckb.explorer.util.I18n;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.nervos.ckb.utils.address.Address;
import org.springframework.stereotype.Service;

@Service
public class OutputServiceImpl extends ServiceImpl<OutputMapper, Output> implements
    OutputService {

  @Resource
  private ScriptMapper scriptMapper;
  @Resource
  private I18n i18n;

  @Override
  public Page<AddressLiveCellsResponse> getAddressLiveCellsByAddress(String address, String tag,
      String sort, Boolean boundStatus, int pageIndex, int pageSize) {
    String[] sortParts = sort.split("\\.", 2);
    String orderBy = sortParts[0];
    String ascOrDesc = sortParts.length > 1 ? sortParts[1].toLowerCase() : "desc";

    // 字段映射
    orderBy = switch (orderBy) {
      case "block_timestamp" -> "block_timestamp";
      case "capacity" -> "capacity";
      default -> throw new ServerException("Invalid order by column");

    };

    List<Long> filteredIds = new ArrayList<>();
    // TODO 根据tag过滤
    if(!StringUtils.isEmpty(tag)){
      switch (tag) {
      case "cellbase":
        case "fiber":
          //filteredIds = outputMapper.getCellbaseDisplayOutputsByTransactionIds(filteredIds);
          break;
        case "multisig":
          //filteredIds = outputMapper.getNormalTxDisplayOutputsByTransactionIds(filteredIds, pageSize);
          break;
        case "deployment":
          //filteredIds = outputMapper.getNormalTxDisplayOutputsByTransactionIds(filteredIds, pageSize);
          break;
        default:
          break;
      }
    }

    Page<AddressLiveCellsResponse> pageResult = new Page<>(pageIndex, pageSize);
    Long lockScriptId = 0L;
    // TODO 比特币的逻辑后面补
    if (boundStatus) {
//      vout_ids = BitcoinVout.where(address_id: address_ids, status: bound_status).pluck(:cell_output_id)
//      CellOutput.live.where(id: vout_ids)

      pageResult.setTotal(0);
      return pageResult;
    }else{
      // 计算地址的哈希
      var addressScriptHash = Address.decode(address).getScript().computeHash();
      LambdaQueryWrapper<Script> queryWrapper = new LambdaQueryWrapper<>();
      queryWrapper.eq(Script::getScriptHash, addressScriptHash);
      var script = scriptMapper.selectOne(queryWrapper);
      if(script == null){
        throw new ServerException(I18nKey.ADDRESS_NOT_FOUND_CODE, i18n.getMessage(I18nKey.ADDRESS_NOT_FOUND_MESSAGE));
      }
      lockScriptId = script.getId();

    }
    var resultPage = baseMapper.getLiveCellsByLockScriptId(pageResult, lockScriptId, filteredIds, orderBy, ascOrDesc);
    return resultPage;
  }
}
