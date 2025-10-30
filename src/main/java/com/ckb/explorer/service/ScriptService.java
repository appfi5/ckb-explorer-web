package com.ckb.explorer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ckb.explorer.domain.resp.AddressResponse;
import com.ckb.explorer.domain.resp.LockScriptResponse;
import com.ckb.explorer.domain.resp.TypeScriptResponse;
import com.ckb.explorer.entity.Script;

public interface ScriptService extends IService<Script> {
  /**
   * 根据地址或锁哈希获取地址详情
   * @param address 地址或哈希
   * @return 地址详情响应对象
   */
  AddressResponse getAddressInfo(String address);

  /**
   * 根据args查询类型脚本
   * @param args
   * @param typeIdCodeHash
   * @return
   */
  TypeScriptResponse findTypeScriptByTypeId(String args, String typeIdCodeHash);

  /**
   * 根据codeHash查询类型脚本
   * @param codeHash
   * @return
   */
  TypeScriptResponse findTypeScriptByCodeHash(String codeHash);

  /**
   * 根据codeHash查询锁脚本
   * @param codeHash
   * @return
   */
  LockScriptResponse findLockScriptByCodeHash(String codeHash);

    Script findByScriptHash(String scriptHash);
}