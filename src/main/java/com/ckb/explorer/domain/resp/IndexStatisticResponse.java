package com.ckb.explorer.domain.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.math.BigDecimal;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 索引统计响应类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
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
     * 平均区块时间 2位精度
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Double averageBlockTime;
    
    /**
     * 当前纪元难度
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger currentEpochDifficulty;
    
    /**
     * 哈希率 6位精度
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal hashRate;
    
    /**
     * 预计纪元时间 6位精度
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal estimatedEpochTime;
    
    /**
     * 过去24小时的交易数
     */
    private Long transactionsLast24hrs;
    
    /**
     * 每分钟交易数
     */
    private Long transactionsCountPerMinute;
}
