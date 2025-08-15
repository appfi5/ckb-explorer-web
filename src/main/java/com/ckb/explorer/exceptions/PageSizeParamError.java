package com.ckb.explorer.exceptions;

/**
 * PageSize 参数异常（对应 Ruby 的 Api::V1::Exceptions::PageSizeParamError）
 */
public class PageSizeParamError extends ApiError {
  // 固定错误参数
  private static final int CODE = 1008;
  private static final int STATUS = 400;
  private static final String TITLE = "Page Size Param Invalid";
  private static final String DETAIL = "Params page size should be an integer";
  private static final String HREF = "https://nervosnetwork.github.io/ckb-explorer/public/api_doc.html";

  // 无参构造器
  public PageSizeParamError() {
    super(CODE, STATUS, TITLE, DETAIL, HREF);
  }
}