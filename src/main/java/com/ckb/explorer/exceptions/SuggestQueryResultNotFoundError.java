package com.ckb.explorer.exceptions;

/**
 * 建议查询结果未找到异常（对应 Ruby 的 Api::V1::Exceptions::SuggestQueryResultNotFoundError）
 */
public class SuggestQueryResultNotFoundError extends ApiError {
  // 固定错误参数（与 Ruby 中 super 传入的参数完全一致）
  private static final int CODE = 1018;
  private static final int STATUS = 404;
  private static final String TITLE = "No matching records found";
  private static final String DETAIL = "No records found by given query key";
  private static final String HREF = "https://nervosnetwork.github.io/ckb-explorer/public/api_doc.html";

  // 无参构造器：初始化固定错误参数
  public SuggestQueryResultNotFoundError() {
    super(CODE, STATUS, TITLE, DETAIL, HREF);
  }
}