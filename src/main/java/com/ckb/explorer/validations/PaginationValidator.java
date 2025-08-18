package com.ckb.explorer.validations;


import com.ckb.explorer.exceptions.ApiError;
import com.ckb.explorer.exceptions.PageParamError;
import com.ckb.explorer.exceptions.PageSizeParamError;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页参数校验器（对应 Ruby 的 Validations::Pagination）
 */
public class PaginationValidator {
  // 分页参数（page：页码，pageSize：每页条数）
  private final Integer page;
  private final Integer pageSize;

  // 构造器：接收外部传入的分页参数（类似 Ruby 的 initialize(params)）
  public PaginationValidator(Integer page, Integer pageSize) {
    this.page = page;
    this.pageSize = pageSize;
  }

  /**
   * 执行参数校验：收集所有失败的异常（核心逻辑）
   * @return 异常集合（无异常则返回空集合）
   */
  public List<ApiError> validate() {
    // 1. 初始化异常集合（对应 Ruby 的 api_errors = []）
    List<ApiError> errors = new ArrayList<>();

    // 2. 校验 page 参数：非整数/小于1 → 加入 PageParamError
    if (!isValidPage()) {
      errors.add(new PageParamError()); // 对应 Ruby 的 errors << PageParamError.new
    }

    // 3. 校验 pageSize 参数：非整数/小于1 → 加入 PageSizeParamError
    if (!isValidPageSize()) {
      errors.add(new PageSizeParamError()); // 对应 Ruby 的 errors << PageSizeParamError.new
    }

    // TODO 防止恶意请求 需确认
//    if (page < 1 || page > 10000) errors.add(...);
//    if (pageSize < 1 || pageSize > 100) errors.add(...); // 限制最大 100

    // 4. 返回收集的异常（后续可处理）
    return errors;
  }

  /**
   * 辅助方法：校验 page 参数合法性（必须是 ≥1 的整数，允许为 null）
   */
  private boolean isValidPage() {
    // 允许为 null（对应 Ruby 的 allow_nil: true）
    if (page == null) {
      return true;
    }
    // 必须是整数（Java 中 Integer 本身是整数，此处校验是否 ≥1）
    return page >= 1;
  }

  /**
   * 辅助方法：校验 pageSize 参数合法性
   */
  private boolean isValidPageSize() {
    if (pageSize == null) {
      return true;
    }
    return pageSize >= 1;
  }

  // Getter：供外部获取参数（可选）
  public Integer getPage() {
    return page;
  }

  public Integer getPageSize() {
    return pageSize;
  }
}