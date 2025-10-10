package com.ckb.explorer.domain.req;

import com.ckb.explorer.domain.req.base.BasePageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ContractTransactionsPageReq 用于合约交易列表的请求参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractTransactionsPageReq extends BasePageReq {

    @Schema(description = "交易哈希")
    private String txHash;

    @Schema(description = "地址哈希")
    private String addressHash;
}