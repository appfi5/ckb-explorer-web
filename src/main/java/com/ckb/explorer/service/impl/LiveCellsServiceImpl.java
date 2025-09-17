package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.config.ScriptConfig;
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

  @Resource
  private ScriptConfig scriptConfig;

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
      result = baseMapper.getOthersLiveCellsByLockScriptId(pageResult,script.getId());
    } else {
      result = baseMapper.getLiveCellsByLockScriptIdWithTypeScriptId(pageResult, script.getId(), typeScriptId);
    }

    result.getRecords().stream().map(liveCells -> {

      var extraInfo = liveCells.getExtraInfo();
      if(extraInfo == null){
        extraInfo = new ExtraInfoResponse();
      }
      extraInfo.setType(CellType.getTypeByCellType(liveCells.getCellType()));
      if (liveCells.getCellType().intValue() == CellType.NORMAL.getValue()) {
        extraInfo.setCapacity(liveCells.getCapacity());
      } else if(liveCells.getCodeHash() != null){
        var typeScript = scriptConfig.getTypeScriptByCodeHash(liveCells.getCodeHash(), liveCells.getArgs());
        if(typeScript != null){
          extraInfo.setSymbol(typeScript == null ? null : typeScript.getSymbol());
          extraInfo.setDecimal(typeScript == null ? null : typeScript.getDecimal().toString());
          extraInfo.setPublished(true);
          extraInfo.setTypeHash(liveCells.getTypeHash());
        }
      }
      liveCells.setExtraInfo(extraInfo);
      return liveCells;
    }).collect(Collectors.toList());
    return result;
  }
}




