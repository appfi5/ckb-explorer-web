package com.ckb.explorer.controller;

import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.domain.resp.IndexStatisticResponse;
import com.ckb.explorer.facade.IStatisticCacheFacade;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticsController {

    private static final String[] NOT_USED_COLUMNS = {"last_n_days_transaction_fee_rates", "pending_transaction_fee_rates", "transaction_fee_rates"};

    @Resource
    private IStatisticCacheFacade statisticCacheFacade;

    /**
     * 获取统计信息索引
     */
    @GetMapping
    @Operation(summary = "获取统计信息索引")
    public ResponseInfo<IndexStatisticResponse> index() {
        // 设置缓存控制头
        // 注意：在实际项目中，可能需要根据Spring版本和配置调整缓存策略
        return ResponseInfo.SUCCESS(statisticCacheFacade.getIndexStatistic());
    }

//    /**
//     * 获取指定统计信息
//     */
//    @GetMapping("/{id}")
//    @Operation(summary = "获取指定统计信息")
//    public ResponseInfo<Object> show(@PathVariable @Valid @NotEmpty String id) {
//        // 验证查询参数
//        validateQueryParams(id);
//
//        return ResponseInfo.SUCCESS(statisticService.getStatisticById(id));
//    }

    /**
     * 验证查询参数
     */
//    private void validateQueryParams(String infoName) {
//        if (StringUtils.isEmpty(infoName)) {
//            throw new ServerException(I18nKey.STATISTIC_INFO_NAME_INVALID_CODE, "Given statistic info name is invalid");
//        }
//
//        // 验证infoName是否为支持的值
//        boolean isValid = statisticService.isValidStatisticInfoName(infoName);
//        if (!isValid) {
//            throw new ServerException(I18nKey.STATISTIC_INFO_NAME_INVALID_CODE, "Given statistic info name is invalid");
//        }
//    }
}