package com.ckb.explorer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ckb.explorer.domain.resp.AddressBalanceRanking;
import com.ckb.explorer.entity.StatisticInfo;
import java.util.List;
import org.nervos.ckb.type.BlockchainInfo;

/**
 * StatisticInfoService 统计信息服务接口
 * 用于操作statistic_infos表相关的业务逻辑
 */
public interface StatisticInfoService extends IService<StatisticInfo> {

  List<AddressBalanceRanking> getAddressBalanceRanking();

  BlockchainInfo getBlockchainInfo();

  StatisticInfo getStatisticInfo();
}