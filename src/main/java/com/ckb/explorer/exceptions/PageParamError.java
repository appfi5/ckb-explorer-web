package com.ckb.explorer.exceptions;

/**
 * Page 参数异常（对应 Ruby 的 Api::V1::Exceptions::PageParamError）
 */
public class PageParamError extends ApiError {
  // 固定错误参数（与 Ruby 中 super 传入的参数完全一致）
  private static final int CODE = 1007;
  private static final int STATUS = 400;
  private static final String TITLE = "Page Param Invalid";
  private static final String DETAIL = "Params page should be an integer";
  private static final String HREF = "https://nervosnetwork.github.io/ckb-explorer/public/api_doc.html";

  // 无参构造器：初始化固定错误参数
  public PageParamError() {
    super(CODE, STATUS, TITLE, DETAIL, HREF);
  }
}