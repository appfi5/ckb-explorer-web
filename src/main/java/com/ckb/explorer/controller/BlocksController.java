package com.ckb.explorer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.common.dto.ErrorDetail;
import com.ckb.explorer.common.dto.ResponsePageInfo;
import com.ckb.explorer.domain.req.BlockPageReq;
import com.ckb.explorer.domain.resp.BlockListResponse;
import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.ckb.explorer.entity.Block;
import com.ckb.explorer.exceptions.ApiError;
import com.ckb.explorer.facade.IBlockCacheFacade;
import com.ckb.explorer.mapstruct.BlockConvert;
import com.ckb.explorer.service.BlockService;
import com.ckb.explorer.validations.PaginationValidator;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/blocks")
public class BlocksController {

@Resource
private IBlockCacheFacade blockCacheFacade;

  @GetMapping
  public Object index(BlockPageReq req) {

    try {
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
                error.getDetail()
            ))
            .toList();
      }

      // 查询带缓存
      return blockCacheFacade.getBlocksByPage(req.getPage(), req.getPageSize(), req.getSort());
    } catch (Exception e) {
      // 记录异常详细信息
      System.err.println("Error in BlocksController.index():");
      e.printStackTrace();
      // 返回简单的错误响应
      return new ErrorDetail(
          500,
          "Internal Server Error",
          e.getMessage() != null ? e.getMessage() : "Unknown error"
      );
    }
  }

}
