package com.ckb.explorer.controller;

import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.resp.LockScriptResponse;
import com.ckb.explorer.entity.Output;
import com.ckb.explorer.entity.Script;
import com.ckb.explorer.mapstruct.LockScriptConvert;
import com.ckb.explorer.service.OutputService;
import com.ckb.explorer.service.ScriptService;
import com.ckb.explorer.util.I18n;
import com.ckb.explorer.util.QueryKeyUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CellOutputLockScriptsController控制器，用于处理CellOutput的lock_script相关请求
 */
@Deprecated
@RestController
@RequestMapping("/api/v1/cell_output_lock_scripts")
public class CellOutputLockScriptsController {

    @Resource
    private OutputService outputService;

    @Resource
    private ScriptService scriptService;

    @Resource
    private QueryKeyUtils queryKeyUtils;

    @Resource
    private I18n i18n;

    /**
     * 获取CellOutput的lock_script信息
     * @param id CellOutput的ID
     * @return 序列化后的lock_script信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取CellOutput的lock_script信息")
    public ResponseInfo<LockScriptResponse> show(@PathVariable String id) {
        // 验证查询参数
        validateQueryParams(id);
        
        // 根据ID查询CellOutput
        Output cellOutput = outputService.getById(Long.parseLong(id));
        if (cellOutput == null) {
            throw new ServerException(I18nKey.CELL_OUTPUT_NOT_FOUND_CODE, i18n.getMessage(I18nKey.CELL_OUTPUT_NOT_FOUND_MESSAGE));
        }
        
        // 获取lock_script
        Script lockScript = scriptService.getById(cellOutput.getLockScriptId());
        if (lockScript == null) {
            throw new ServerException(I18nKey.SCRIPT_NOT_FOUND_CODE, i18n.getMessage(I18nKey.SCRIPT_NOT_FOUND_MESSAGE));
        }
        
        // 序列化并返回
        return ResponseInfo.SUCCESS(LockScriptConvert.INSTANCE.toConvert(lockScript));
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