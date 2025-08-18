package com.ckb.explorer.exceptions;

/**
 * 不可接受的媒体类型异常（对应 Ruby 的 Api::V1::Exceptions::InvalidAcceptError）
 */
public class InvalidAcceptError extends ApiError {
  // 固定错误参数（与 Ruby 中 super 传入的参数完全一致）
  private static final int CODE = 1002;
  private static final int STATUS = 406;
  private static final String TITLE = "Not Acceptable";
  private static final String DETAIL = "Accept must be application/vnd.api+json";
  private static final String HREF = "https://nervosnetwork.github.io/ckb-explorer/public/api_doc.html";

  // 无参构造器：初始化固定错误参数
  public InvalidAcceptError() {
    super(CODE, STATUS, TITLE, DETAIL, HREF);
  }
}