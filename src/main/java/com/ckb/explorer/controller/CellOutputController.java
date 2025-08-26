package com.ckb.explorer.controller;

import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.resp.CellInfoResponse;
import com.ckb.explorer.domain.resp.CellOutputDataResponse;
import com.ckb.explorer.entity.Output;
import com.ckb.explorer.entity.Script;
import com.ckb.explorer.facade.ICellInfoCacheFacade;
import com.ckb.explorer.mapstruct.LockScriptConvert;
import com.ckb.explorer.mapstruct.TypeScriptConvert;
import com.ckb.explorer.service.OutputService;
import com.ckb.explorer.service.ScriptService;
import com.ckb.explorer.util.I18n;
import com.ckb.explorer.util.QueryKeyUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.nervos.ckb.utils.Numeric;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cell_output")
public class CellOutputController {

  @Resource
  private QueryKeyUtils queryKeyUtils;

  @Resource
  private I18n i18n;

  @Resource
  private ICellInfoCacheFacade cellInfoCacheFacade;

  /**
   * 获取CellInfo的数据
   * @param id CellOutput的ID
   * @return 序列化后的CellInfo数据
   */
  @GetMapping("/{id}")
  @Operation(summary = "获取CellInfo的数据")
  public ResponseInfo<CellInfoResponse> show(@PathVariable String id) {
    // 验证查询参数
    validateQueryParams(id);

    CellInfoResponse response = cellInfoCacheFacade.findByOutputId(id);
    if (response == null) {
      throw new ServerException(I18nKey.CELL_OUTPUT_NOT_FOUND_CODE, i18n.getMessage(I18nKey.CELL_OUTPUT_NOT_FOUND_MESSAGE));
    }

    // 序列化并返回
    return ResponseInfo.SUCCESS(response);
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
