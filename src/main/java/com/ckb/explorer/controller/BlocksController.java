package com.ckb.explorer.controller;

import com.ckb.explorer.common.dto.ErrorDetail;
import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.domain.req.BlockPageReq;
import com.ckb.explorer.domain.resp.BlockResponse;
import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.ckb.explorer.exceptions.ApiError;
import com.ckb.explorer.exceptions.BlockQueryKeyInvalidError;
import com.ckb.explorer.facade.IBlockCacheFacade;
import com.ckb.explorer.util.QueryKeyUtils;
import com.ckb.explorer.validations.PaginationValidator;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/blocks")
public class BlocksController {

  @Resource
  private IBlockCacheFacade blockCacheFacade;

  @Resource
  private QueryKeyUtils queryKeyUtils;

  /**
   * 查询块列表
   * @param req
   * @return
   */
  @GetMapping
  @Operation(summary = "获取块列表")
  public Object index(BlockPageReq req) {

    // 1. 创建分页校验器，传入参数
    PaginationValidator validator = new PaginationValidator(req.getPage(), req.getPageSize());
    // 2. 执行校验：收集异常（对应 Ruby 的 validator.invalid? + 收集 errors）
    List<ApiError> errors = validator.validate();

    // 3. 若有异常：返回错误响应（状态码 + 错误列表）
    if (!errors.isEmpty()) {
      return errors.stream()
          .map(error -> new ErrorDetail(
              error.getCode(),
              error.getTitle(),
              error.getDetail(),
              error.getStatus()
          ))
          .toList();
    }

    // 查询带缓存
    return blockCacheFacade.getBlocksByPage(req.getPage(), req.getPageSize(), req.getSort());

  }

  /**
   * 查询块信息
   * @param id
   * @return
   */
  @GetMapping("/blocks/{id}")
  @Operation(summary = "获取块详情")
  public ResponseInfo<BaseResponse<BlockResponse>> show(@PathVariable String id) {

    // 校验入参
    if(StringUtils.isEmpty(id) || (!queryKeyUtils.isIntegerString(id) && !queryKeyUtils.isValidHex(id))){
      throw new BlockQueryKeyInvalidError();
    }

    // 查询带缓存
    return blockCacheFacade.findBlock(id);

  }
}
