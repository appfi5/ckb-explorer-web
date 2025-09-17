package com.ckb.explorer.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.domain.req.UdtTransactionsPageReq;
import com.ckb.explorer.domain.resp.UdtTransactionPageResponse;
import com.ckb.explorer.facade.IUdtTransactionCacheFacade;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/udt_transactions")
@RestController
public class UdtTransactionsController {
    @Resource
    IUdtTransactionCacheFacade iUdtTransactionCacheFacade;

    @GetMapping("/{typeScriptHash}")
    @Operation(summary = "UDT交易列表")
    public ResponseInfo<Page<UdtTransactionPageResponse>> index(@PathVariable String typeScriptHash, UdtTransactionsPageReq req) {
        return ResponseInfo.SUCCESS(iUdtTransactionCacheFacade.getUdtTransactions(typeScriptHash, req.getSort(), req.getPage(), req.getPageSize()));
    }

}
