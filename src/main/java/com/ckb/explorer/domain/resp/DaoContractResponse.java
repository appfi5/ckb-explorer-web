package com.ckb.explorer.domain.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DAO合约响应类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DaoContractResponse {
    private String type = "nervos_dao";
    private String totalDeposit;
    private String depositorsCount;
    private String depositChanges;
    private String unclaimedCompensationChanges;
    private String claimedCompensationChanges;
    private String depositorChanges;
    private String unclaimedCompensation;
    private String claimedCompensation;
    private String averageDepositTime;
    private String miningReward;
    private String depositCompensation;
    private String treasuryAmount;
    private String estimatedApc;
}