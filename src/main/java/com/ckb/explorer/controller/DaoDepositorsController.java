package com.ckb.explorer.controller;

import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.domain.resp.DaoDepositorResponse;
import com.ckb.explorer.facade.IDaoDepositorCacheFacade;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * DAO存款者控制器
 * 对应Ruby代码中的Api::V1::DaoDepositorsController
 */
@RestController
@RequestMapping("/api/v1/dao_depositors")
public class DaoDepositorsController {

    @Resource
    private IDaoDepositorCacheFacade daoDepositorCacheFacade;

    /**
     * 获取DAO存款者列表
     * 对应Ruby代码中的index方法
     * @return DAO存款者列表响应
     */
    @GetMapping
    public ResponseInfo<List<DaoDepositorResponse>> index() {
        // 调用服务层获取前100名DAO存款者列表
        List<DaoDepositorResponse> depositors = daoDepositorCacheFacade.getTopDaoDepositors();
        return ResponseInfo.SUCCESS(depositors);
    }
}
