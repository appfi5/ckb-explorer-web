package com.ckb.explorer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ckb.explorer.domain.resp.DaoContractResponse;
import com.ckb.explorer.entity.DaoContract;


/**
 * DAO合约接口类
 */
public interface DaoContractService extends IService<DaoContract> {

    /**
     * 获取默认DAO合约数据
     * 对应Ruby代码中的DaoContract.default_contract
     * 
     * @return DAO合约响应数据
     */
    DaoContractResponse getDefaultContract();

}