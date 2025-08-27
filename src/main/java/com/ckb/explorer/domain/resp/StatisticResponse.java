package com.ckb.explorer.domain.resp;

import lombok.Data;

/**
 * 统计响应类
 */
@Data
public class StatisticResponse {
    
    /**
     * 最新区块高度
     */
    private String tipBlockNumber;
    
    /**
     * 平均区块时间
     */
    private String averageBlockTime;
    
    /**
     * 当前纪元难度
     */
    private String currentEpochDifficulty;
    
    /**
     * 哈希率
     */
    private String hashRate;
    
    /**
     * 矿工排名
     */
    private Object minerRanking;
    
    /**
     * 区块链信息
     */
    private Object blockchainInfo;
    
    /**
     * 缓存刷新信息
     */
    private Object flushCacheInfo;
    
    /**
     * 地址余额排名
     */
    private Object addressBalanceRanking;
    
    /**
     * 维护信息
     */
    private Object maintenanceInfo;
    
    /**
     * 创建时间戳（UNIX格式）
     */
    private String createdAtUnixtimestamp;
}
