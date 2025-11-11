package com.ckb.explorer.domain.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DAO存款者响应类
 * 对应Ruby代码中的DaoDepositorSerializer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DaoDepositorResponse {
    private String type = "dao_depositor";

    /**
     * 地址id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 地址哈希
     */
    private String addressHash;
    
    /**
     * DAO存款金额
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger daoDeposit;
    
    /**
     * 平均存款时间 当前不要
     */
    // private String averageDepositTime;
}