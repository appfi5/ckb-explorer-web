package com.ckb.explorer.controller;

import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.resp.CellOutputDataResponse;
import com.ckb.explorer.entity.Output;
import com.ckb.explorer.service.OutputService;
import com.ckb.explorer.util.I18n;
import com.ckb.explorer.util.QueryKeyUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.nervos.ckb.utils.Numeric;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CellOutputDataController控制器，用于处理CellOutput的数据相关请求
 */
@RestController
@RequestMapping("/api/v1/cell_output_data")
@Validated
public class CellOutputDataController {

    @Resource
    private OutputService outputService;

    @Resource
    private QueryKeyUtils queryKeyUtils;

    @Resource
    private I18n i18n;

    private static final int MAXIMUM_DOWNLOADABLE_SIZE = 64000;

    /**
     * 获取CellOutput的数据
     * @param id CellOutput的ID
     * @return 序列化后的CellOutput数据
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取CellOutput的数据")
    public ResponseInfo<CellOutputDataResponse> show(@PathVariable String id) {
        // 验证查询参数
        validateQueryParams(id);

        // 根据ID查询CellOutput
        Output cellOutput = outputService.getById(Long.parseLong(id));
        if (cellOutput == null) {
            throw new ServerException(I18nKey.CELL_OUTPUT_NOT_FOUND_CODE, i18n.getMessage(I18nKey.CELL_OUTPUT_NOT_FOUND_MESSAGE));
        }

        // 检查数据大小是否超过限制
        if (cellOutput.getData() != null && cellOutput.getData().length > MAXIMUM_DOWNLOADABLE_SIZE) {
            throw new ServerException(I18nKey.CELL_OUTPUT_DATA_SIZE_EXCEEDS_LIMIT_CODE, i18n.getMessage(I18nKey.CELL_OUTPUT_DATA_SIZE_EXCEEDS_LIMIT_MESSAGE));
        }

        // 序列化并返回
        return ResponseInfo.SUCCESS(new CellOutputDataResponse(cellOutput.getData() != null ? Numeric.toHexString(cellOutput.getData()) : null));
    }

    /**
     * 验证查询参数
     * @param id 要验证的ID
     */
    private void validateQueryParams(String id) {
        // 检查ID是否为空或不是有效的数字
        if (StringUtils.isEmpty(id) || !queryKeyUtils.isIntegerString(id)) {
            throw new ServerException(I18nKey.CELL_OUTPUT_ID_INVALID_CODE, i18n.getMessage(I18nKey.CELL_OUTPUT_ID_INVALID_MESSAGE));
        }
    }
}