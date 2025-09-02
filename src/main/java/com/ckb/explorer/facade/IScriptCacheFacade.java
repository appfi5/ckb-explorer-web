package com.ckb.explorer.facade;

import com.ckb.explorer.domain.resp.AddressResponse;

public interface IScriptCacheFacade {
  /**
   * 根据地址或Script哈希获取地址详情
   * @param address 地址或哈希
   * @return 地址详情响应对象
   */
  AddressResponse getAddressInfo(String address);
}
