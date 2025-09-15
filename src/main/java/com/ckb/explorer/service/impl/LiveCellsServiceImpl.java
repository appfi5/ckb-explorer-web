package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.resp.ExtraInfoResponse;
import com.ckb.explorer.domain.resp.LiveCellsResponse;
import com.ckb.explorer.entity.LiveCells;
import com.ckb.explorer.entity.Script;
import com.ckb.explorer.enums.CellType;
import com.ckb.explorer.mapper.LiveCellsMapper;
import com.ckb.explorer.mapper.ScriptMapper;
import com.ckb.explorer.service.LiveCellsService;
import com.ckb.explorer.util.I18n;
import com.ckb.explorer.util.QueryKeyUtils;
import jakarta.annotation.Resource;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author dell
 * @description 针对表【live_cells】的数据库操作Service实现
 * @createDate 2025-09-02 13:43:49
 */
@Service
@Slf4j
@Transactional
public class LiveCellsServiceImpl extends ServiceImpl<LiveCellsMapper, LiveCells>
    implements LiveCellsService {

  @Resource
  private QueryKeyUtils queryKeyUtils;

  @Resource
  private I18n i18n;

  @Resource
  private ScriptMapper scriptMapper;


  @Override
  public Page<LiveCellsResponse> getLiveCellsByAddressWithTypeHash(String address, String typeHash,
      int page, int pageSize) {
    LambdaQueryWrapper<Script> queryWrapper = new LambdaQueryWrapper<>();
    // 检查是否为有效的十六进制字符串
    if (queryKeyUtils.isValidHex(address)) {
      queryWrapper.eq(Script::getScriptHash, Numeric.hexStringToByteArray(address));
    } else
      // 检查是否为有效的地址
      if (queryKeyUtils.isValidAddress(address)) {
        // 计算地址的哈希
        var addressScriptHash = Address.decode(address).getScript().computeHash();
        queryWrapper.eq(Script::getScriptHash, addressScriptHash);
      } else {
        throw new ServerException(I18nKey.ADDRESS_NOT_FOUND_CODE,
            i18n.getMessage(I18nKey.ADDRESS_NOT_FOUND_MESSAGE));
      }

    var script = scriptMapper.selectOne(queryWrapper);
    if (script == null) {
      throw new ServerException(I18nKey.ADDRESS_NOT_FOUND_CODE,
          i18n.getMessage(I18nKey.ADDRESS_NOT_FOUND_MESSAGE));
    }

    Long typeScriptId = null;
    if(!"ckb".equals(typeHash) && !"others".equals(typeHash)){
      LambdaQueryWrapper<Script> typeQueryWrapper = new LambdaQueryWrapper<>();
      typeQueryWrapper.eq(Script::getScriptHash, Numeric.hexStringToByteArray(typeHash));
      var typeScript = scriptMapper.selectOne(typeQueryWrapper);
      if (typeScript == null) {
        throw new ServerException(I18nKey.SCRIPT_NOT_FOUND_CODE,
            i18n.getMessage(I18nKey.SCRIPT_NOT_FOUND_MESSAGE));
      }
      typeScriptId = typeScript.getId();
    }

    Page<LiveCellsResponse> pageResult = new Page<>(page, pageSize);
    Page<LiveCellsResponse> result;
    if("others".equals(typeHash)){
      Set<Integer> udtAndNormalCellType = Set.of(CellType.NORMAL.getValue(), CellType.UDT.getValue(),CellType.XUDT.getValue(),CellType.XUDT_COMPATIBLE.getValue());
      result = baseMapper.getOthersLiveCellsByLockScriptId(pageResult,script.getId(), udtAndNormalCellType);
    } else {
      result = baseMapper.getLiveCellsByLockScriptIdWithTypeScriptId(pageResult, script.getId(), typeScriptId);
    }

    result.getRecords().stream().map(liveCells -> {
      var extraInfo = liveCells.getExtraInfo();
      if(extraInfo == null){
        extraInfo = new ExtraInfoResponse();
      }
      extraInfo.setType(getTypeByCellType(liveCells.getCellType()));
      if (liveCells.getCellType().intValue() == CellType.NORMAL.getValue()) {
        extraInfo.setCapacity(liveCells.getCapacity());
      }
      return liveCells;
    }).collect(Collectors.toList());
    return result;
  }

  private String getTypeByCellType(Integer cellType){
    var type =CellType.valueOf(cellType);
    switch (type) {
      case CellType.NORMAL:
        return "ckb";
      case CellType.UDT:
        return type.getName();
      case CellType.XUDT:
        return "xudt";
      case CellType.XUDT_COMPATIBLE:
        return "xudt_compatible";
      case CellType.COTA_REGISTRY:
      case CellType.COTA_REGULAR:
        return "cota";
      case CellType.M_NFT_ISSUER:
      case CellType.M_NFT_CLASS:
      case CellType.M_NFT_TOKEN:
        return "m_nft";
      case CellType.NRC_721_TOKEN:
      case CellType.NRC_721_FACTORY:
        return "nrc_721";
      case CellType.SPORE_CLUSTER:
      case CellType.SPORE_CELL:
      case CellType.DID_CELL:
        return "dob";
      case CellType.OMIGA_INSCRIPTION_INFO:
      case CellType.OMIGA_INSCRIPTION:
        return "omiga_inscription";
      case CellType.STABLEPP_POOL:
        return "stablepp_pool";
      default:
        return "other";
    }
  }
}




