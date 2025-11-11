package com.ckb.explorer.domain.resp;

import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ContractTransactionPageResponse 用于将合约交易实体序列化为 JSON 响应对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractTransactionPageResponse extends BaseResponse<Long> {
    private String type = "ckb_transactions";

    private Boolean isCellbase;

    private String transactionHash;

    private Long blockNumber;

    private Long blockTimestamp;

    private int displayInputsCount;

    private int displayOutputsCount;

    private List<CellInputResponse> displayInputs;

    private List<CellOutputResponse> displayOutputs;
}