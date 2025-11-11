package com.ckb.explorer.domain.dto;

import java.math.BigInteger;
import lombok.Data;

/**
 * DAO存款者数据传输对象
 * 用于封装DAO存款者查询结果
 */
@Data
public class DaoDepositorDto {

    /**
     * 地址
     */
    private Long lockScriptId;
    
    /**
     * DAO存款金额
     */
    private BigInteger daoDeposit;
}