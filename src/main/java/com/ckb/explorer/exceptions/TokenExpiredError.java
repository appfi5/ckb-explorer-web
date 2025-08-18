package com.ckb.explorer.exceptions;

/**
 * 令牌过期异常（对应 Ruby 的 Api::V1::Exceptions::TokenExpiredError）
 */
public class TokenExpiredError extends ApiError {
  // 固定错误参数（与 Ruby 中 super 传入的参数完全一致）
  private static final int CODE = 1034;
  private static final int STATUS = 400;
  private static final String TITLE = "Token has expired";
  private static final String DETAIL = "";
  private static final String HREF = "https://nervosnetwork.github.io/ckb-explorer/public/api_doc.html";

  // 无参构造器：初始化固定错误参数
  public TokenExpiredError() {
    super(CODE, STATUS, TITLE, DETAIL, HREF);
  }
}