package com.ckb.explorer.exceptions;

/**
 * 不支持的媒体类型异常（对应 Ruby 的 Api::V1::Exceptions::InvalidContentTypeError）
 */
public class InvalidContentTypeError extends ApiError {
  // 固定错误参数（与 Ruby 中 super 传入的参数完全一致）
  private static final int CODE = 1001;
  private static final int STATUS = 415;
  private static final String TITLE = "Unsupported Media Type";
  private static final String DETAIL = "Content Type must be application/vnd.api+json";
  private static final String HREF = "https://nervosnetwork.github.io/ckb-explorer/public/api_doc.html";

  // 无参构造器：初始化固定错误参数
  public InvalidContentTypeError() {
    super(CODE, STATUS, TITLE, DETAIL, HREF);
  }
}