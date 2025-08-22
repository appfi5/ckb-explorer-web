package com.ckb.explorer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.req.BlockPageReq;
import com.ckb.explorer.domain.resp.BlockListResponse;
import com.ckb.explorer.domain.resp.BlockResponse;
import com.ckb.explorer.facade.IBlockCacheFacade;
import com.ckb.explorer.util.I18n;
import com.ckb.explorer.util.QueryKeyUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/blocks")
@Validated
public class BlocksController {

  @Resource
  private IBlockCacheFacade blockCacheFacade;

  @Resource
  private QueryKeyUtils queryKeyUtils;

  @Resource
  private I18n i18n;

  /**
   * 查询块列表
   * @param req
   * @return
   */
  @GetMapping
  @Operation(summary = "获取块列表")
  public ResponseInfo<Page<BlockListResponse>> index(@Valid BlockPageReq req) {

    // 查询带缓存
    return ResponseInfo.SUCCESS(blockCacheFacade.getBlocksByPage(req.getPage(), req.getPageSize(), req.getSort()));

  }

  /**
   * 查询块信息
   * @param id
   * @return
   */
  @GetMapping("/{id}")
  @Operation(summary = "获取块详情")
  public ResponseInfo<BlockResponse> show(@PathVariable String id) {

    // 校验入参
    if(StringUtils.isEmpty(id) || (!queryKeyUtils.isIntegerString(id) && !queryKeyUtils.isValidHex(id))){
      throw new ServerException(I18nKey.BLOCK_QUERY_KEY_INVALID_CODE, i18n.getMessage(I18nKey.BLOCK_QUERY_KEY_INVALID_MESSAGE));
    }

    // 查询带缓存
    return ResponseInfo.SUCCESS(blockCacheFacade.findBlock(id));

  }
}
