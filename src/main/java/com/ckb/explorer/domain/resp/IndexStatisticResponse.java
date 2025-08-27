package com.ckb.explorer.domain.resp;

import java.math.BigInteger;
import lombok.Data;

/**
 * 索引统计响应类
 */
@Data
public class IndexStatisticResponse {

  private String type = "index_statistic";
    /**
     * 纪元信息
     */
    private EpochInfoResponse epochInfo;
    
    /**
     * 最新区块高度
     */
    private Long tipBlockNumber;
    
    /**
     * 平均区块时间
     */
    private String averageBlockTime;
    
    /**
     * 当前纪元难度
     */
    private BigInteger currentEpochDifficulty;
    
    /**
     * 哈希率
     */
    private String hashRate;
    
    /**
     * 预计纪元时间
     */
    private String estimatedEpochTime;
    
    /**
     * 过去24小时的交易数
     */
    private Long transactionsLast24hrs;
    
    /**
     * 每分钟交易数
     */
    private Long transactionsCountPerMinute;
}
