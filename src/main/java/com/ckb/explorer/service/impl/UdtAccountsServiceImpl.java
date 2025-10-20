package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.config.ScriptConfig;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.dto.AccountUdtBalanceDto;
import com.ckb.explorer.domain.resp.AccountUdtBalanceResponse;
import com.ckb.explorer.entity.Script;
import com.ckb.explorer.entity.UdtAccounts;
import com.ckb.explorer.mapper.ScriptMapper;
import com.ckb.explorer.mapper.UdtAccountsMapper;
import com.ckb.explorer.mapstruct.AccountUdtBalanceConvert;
import com.ckb.explorer.service.UdtAccountsService;
import com.ckb.explorer.util.I18n;
import com.ckb.explorer.util.QueryKeyUtils;
import jakarta.annotation.Resource;
import java.util.List;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;
import org.springframework.stereotype.Service;

@Service
public class UdtAccountsServiceImpl extends ServiceImpl<UdtAccountsMapper, UdtAccounts> implements
    UdtAccountsService {

  @Resource
  private UdtAccountsMapper udtAccountsMapper;
  @Resource
  private ScriptMapper scriptMapper;
  @Resource
  private QueryKeyUtils queryKeyUtils;
  @Resource
  private I18n i18n;
  @Resource
  private ScriptConfig scriptConfig;
  @Override
  public List<AccountUdtBalanceResponse> getUdtBalanceByAddress(String address) {
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
      }else {
        throw new ServerException(I18nKey.ADDRESS_NOT_FOUND_CODE, i18n.getMessage(I18nKey.ADDRESS_NOT_FOUND_MESSAGE));
      }
    var script = scriptMapper.selectOne(queryWrapper);
    if(script == null){
      throw new ServerException(I18nKey.ADDRESS_NOT_FOUND_CODE, i18n.getMessage(I18nKey.ADDRESS_NOT_FOUND_MESSAGE));
    }
    List<AccountUdtBalanceDto> list = udtAccountsMapper.getUdtBalanceByLockScriptId(script.getId());

    List<AccountUdtBalanceResponse> result = AccountUdtBalanceConvert.INSTANCE.toConvert(list);
    result.forEach(item -> {
      var typeScript = scriptConfig.getTypeScriptById(item.getTypeScriptId());
      if(typeScript != null){
        item.setFullName(typeScript.getName());
        item.setSymbol(typeScript.getSymbol());
        item.setDecimal(typeScript.getDecimal());
        //item.setUdtIconFile(typeScript.getUdtIconFile());
        item.setUdtType(typeScript.getCellType()); //cellType 同udtType
        item.setTypeScriptHash(typeScript.getScriptHash());
      }
    });
    return result;
  }
}
