package com.ckb.explorer.domain.req;

import com.ckb.explorer.domain.req.base.BasePageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * UdtTransactionsPageReq 用于地址交易列表的请求参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UdtTransactionsPageReq extends BasePageReq {
    
    @Schema(description = "排序方式，默认为time.desc", example = "time.desc")
    private String sort = "time.desc";


    @Schema(description = "交易哈希")
    private String txHash;

    @Schema(description = "地址")
    private String addressHash;


}