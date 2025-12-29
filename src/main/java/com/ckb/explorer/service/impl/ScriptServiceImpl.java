package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.config.ScriptConfig;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.resp.AddressResponse;
import com.ckb.explorer.domain.resp.LockScriptResponse;
import com.ckb.explorer.domain.resp.TypeScriptResponse;
import com.ckb.explorer.entity.Script;
import com.ckb.explorer.entity.StatisticAddress;
import com.ckb.explorer.mapper.ScriptMapper;
import com.ckb.explorer.mapstruct.LockScriptConvert;
import com.ckb.explorer.mapstruct.TypeScriptConvert;
import com.ckb.explorer.service.DepositCellService;
import com.ckb.explorer.service.ScriptService;
import com.ckb.explorer.service.StatisticAddressService;
import com.ckb.explorer.service.WithdrawCellService;
import com.ckb.explorer.util.I18n;
import com.ckb.explorer.util.QueryKeyUtils;
import jakarta.annotation.Resource;
import java.util.List;
import org.nervos.ckb.utils.Numeric;
import org.nervos.ckb.utils.address.Address;
import org.springframework.stereotype.Service;

@Service
public class ScriptServiceImpl extends ServiceImpl<ScriptMapper, Script> implements
    ScriptService {

  @Resource
  private QueryKeyUtils queryKeyUtils;

  @Resource
  private I18n i18n;
  
  @Resource
  private StatisticAddressService statisticAddressService;

  @Resource
  private DepositCellService depositCellService;

  @Resource
  private WithdrawCellService withdrawCellService;

  @Resource
  private ScriptConfig scriptConfig;

  @Override
  public AddressResponse getAddressInfo(String address) {
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


    } // 比特币地址 一期不做比特币
//    else if(address){
//
//    }
    else {
      throw new ServerException(I18nKey.ADDRESS_NOT_FOUND_CODE, i18n.getMessage(I18nKey.ADDRESS_NOT_FOUND_MESSAGE));
    }

    var script = baseMapper.selectOne(queryWrapper);
    if(script == null){
      throw new ServerException(I18nKey.ADDRESS_NOT_FOUND_CODE, i18n.getMessage(I18nKey.ADDRESS_NOT_FOUND_MESSAGE));
    }

    // 获取地址响应对象
    AddressResponse addressResponse = LockScriptConvert.INSTANCE.toConvertAddressResponse(script);
    var lockScript = addressResponse.getLockScript();
    // 获取codeHash对应的名称
    var scriptWithName = scriptConfig.getLockScriptByCodeHash(lockScript.getCodeHash());
    lockScript.setVerifiedScriptName(scriptWithName == null ? null : scriptWithName.getName());

    // 查询地址统计信息
    var addressStatistics = statisticAddressService.getOne(
        new LambdaQueryWrapper<StatisticAddress>()
            .eq(StatisticAddress::getLockScriptId, script.getId()));
    // 设置地址统计信息
    if (addressStatistics != null) {
      addressResponse.setBalance(addressStatistics.getBalance());
      addressResponse.setLiveCellsCount(addressStatistics.getLiveCellsCount());
      addressResponse.setBalanceOccupied(addressStatistics.getBalanceOccupied());
    }

    // 查询地址的Dao存款
    var deposit = depositCellService.getDepositByLockScriptId(script.getId());
    if (deposit != null) {
      addressResponse.setDaoDeposit(deposit);
    }

    // 查询地址的Dao存款的阶段1未领取的补偿
    addressResponse.setPhase1UnClaimedCompensation(withdrawCellService.phase1DaoInterestsByLockScriptId(script.getId()));
    addressResponse.setDepositUnmadeCompensation(depositCellService.unmadeDaoInterestsByLockScriptId(script.getId()));
    
    return addressResponse;
  }

  @Override
  public TypeScriptResponse findTypeScriptByTypeId(String args, String typeIdCodeHash) {
    LambdaQueryWrapper<Script> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Script::getCodeHash, Numeric.hexStringToByteArray(typeIdCodeHash));
    queryWrapper.eq(Script::getArgs, Numeric.hexStringToByteArray(args));
    queryWrapper.eq(Script::getIsTypescript, 1);
    var script = baseMapper.selectOne(queryWrapper);
    if(script == null){
      return null;
    }
    var typeScriptResponse = TypeScriptConvert.INSTANCE.toConvert(script);
    var scriptConf = scriptConfig.getTypeScriptByCodeHash(typeScriptResponse.getCodeHash(), typeScriptResponse.getArgs());
    typeScriptResponse.setVerifiedScriptName(scriptConf == null ? null : scriptConf.getName());
    return typeScriptResponse;
  }

  @Override
  public TypeScriptResponse findTypeScriptByCodeHash(String codeHash) {
    LambdaQueryWrapper<Script> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Script::getCodeHash, Numeric.hexStringToByteArray(codeHash));
    queryWrapper.eq(Script::getIsTypescript, 1);
    queryWrapper.last("limit 1");
    Script script = baseMapper.selectOne(queryWrapper);
    if(script == null){
      return null;
    }
    var typeScriptResponse = TypeScriptConvert.INSTANCE.toConvert(script);
    var typeScript = scriptConfig.getTypeScriptByCodeHash(codeHash, typeScriptResponse.getArgs());
    typeScriptResponse.setVerifiedScriptName(typeScript == null ? null : typeScript.getName());
    return typeScriptResponse;
  }

  @Override
  public LockScriptResponse findLockScriptByCodeHash(String codeHash) {
    LambdaQueryWrapper<Script> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Script::getCodeHash, Numeric.hexStringToByteArray(codeHash));
    queryWrapper.eq(Script::getIsTypescript, 0);
    queryWrapper.last("limit 1");
    Script script = baseMapper.selectOne(queryWrapper);
    if(script == null){
      return null;
    }
    var lockScript = scriptConfig.getLockScriptByCodeHash(codeHash);
    LockScriptResponse lockScriptResponse = LockScriptConvert.INSTANCE.toConvert(script);
    lockScriptResponse.setVerifiedScriptName(lockScript == null ? null : lockScript.getName());
    return lockScriptResponse;
  }


  @Override
  public Script findByScriptHash(String scriptHash){
    LambdaQueryWrapper<Script> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Script::getScriptHash, Numeric.hexStringToByteArray(scriptHash));
    Script script = baseMapper.selectOne(queryWrapper);
    return script;
  }

  @Override
  public Script getAddress(String addressHash) {
    // 计算地址的哈希
    var addressScriptHash = Address.decode(addressHash).getScript().computeHash();
    LambdaQueryWrapper<Script> queryScriptWrapper = new LambdaQueryWrapper<>();
    queryScriptWrapper.eq(Script::getScriptHash, addressScriptHash);
    return baseMapper.selectOne(queryScriptWrapper);
  }

  @Override
  public Script getDaoTypeScript(List<byte[]> codeHash){
    LambdaQueryWrapper<Script> queryScriptWrapper = new LambdaQueryWrapper<>();
    queryScriptWrapper.in(Script::getCodeHash, codeHash);
    queryScriptWrapper.eq(Script::getIsTypescript, 1);
    queryScriptWrapper.eq(Script::getHashType, 1);
    queryScriptWrapper.last("limit 1");
    return baseMapper.selectOne(queryScriptWrapper);
  }
}
