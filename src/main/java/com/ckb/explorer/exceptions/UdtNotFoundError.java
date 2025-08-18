package com.ckb.explorer.exceptions;

/**
 * UDT未找到异常（对应 Ruby 的 Api::V1::Exceptions::UdtNotFoundError）
 */
public class UdtNotFoundError extends ApiError {
  // 固定错误参数（与 Ruby 中 super 传入的参数完全一致）
  private static final int CODE = 1026;
  private static final int STATUS = 404;
  private static final String TITLE = "UDT Not Found";
  private static final String DETAIL = "No UDT records found by given type hash";
  private static final String HREF = "https://nervosnetwork.github.io/ckb-explorer/public/api_doc.html";

  // 无参构造器：初始化固定错误参数
  public UdtNotFoundError() {
    super(CODE, STATUS, TITLE, DETAIL, HREF);
  }
}