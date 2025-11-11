package com.ckb.explorer.mapstruct;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.resp.ContractTransactionPageResponse;
import com.ckb.explorer.entity.CkbTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * ContractTransactionConvert 提供 CkbTransaction 实体与 ContractTransactionPageResponse 的转换功能
 */
@Mapper
public interface ContractTransactionConvert {

    ContractTransactionConvert INSTANCE = Mappers.getMapper(ContractTransactionConvert.class);

    /**
     * 将 CkbTransaction 实体转换为 ContractTransactionPageResponse
     *
     * @param transaction CkbTransaction 实体
     * @return ContractTransactionPageResponse
     */
    @Mappings({

    })
    ContractTransactionPageResponse toConvertContractTransactionResponse(CkbTransaction transaction);

    /**
     * 将 Page<CkbTransaction> 转换为 Page<ContractTransactionPageResponse>
     *
     * @param page Page<CkbTransaction>
     * @return Page<ContractTransactionPageResponse>
     */
    Page<ContractTransactionPageResponse> toConvertPage(Page<CkbTransaction> page);
}