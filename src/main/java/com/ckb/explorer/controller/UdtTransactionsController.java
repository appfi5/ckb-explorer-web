package com.ckb.explorer.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.req.UdtTransactionsPageReq;
import com.ckb.explorer.domain.resp.UdtTransactionPageResponse;
import com.ckb.explorer.facade.IUdtTransactionCacheFacade;
import com.ckb.explorer.util.I18n;
import com.ckb.explorer.util.QueryKeyUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/udt_transactions")
@RestController
public class UdtTransactionsController {
    @Resource
    IUdtTransactionCacheFacade iUdtTransactionCacheFacade;

    @Resource
    private QueryKeyUtils queryKeyUtils;

    @Resource
    private I18n i18n;

    @GetMapping("/{typeScriptHash}")
    @Operation(summary = "UDT交易列表")
    public ResponseInfo<Page<UdtTransactionPageResponse>> index(@PathVariable String typeScriptHash, UdtTransactionsPageReq req) {
        if (StringUtils.hasLength(req.getAddressHash()) && !queryKeyUtils.isValidAddress(req.getAddressHash())) {
            throw new ServerException(I18nKey.ADDRESS_HASH_INVALID_CODE,
                    i18n.getMessage(I18nKey.ADDRESS_HASH_INVALID_MESSAGE));
        }
        if (StringUtils.hasLength(req.getTxHash()) && (!queryKeyUtils.isValidHex(req.getTxHash()))) {
            throw new ServerException(I18nKey.CKB_TRANSACTION_TX_HASH_INVALID_CODE, i18n.getMessage(I18nKey.CKB_TRANSACTION_TX_HASH_INVALID_MESSAGE));
        }
        return ResponseInfo.SUCCESS(iUdtTransactionCacheFacade.getUdtTransactions(typeScriptHash, req));
    }

}
