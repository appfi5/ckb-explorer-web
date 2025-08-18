package com.ckb.explorer.exceptions;

/**
 * UDT信息参数无效异常（对应 Ruby 的 Api::V1::Exceptions::UdtInfoInvalidError）
 */
public class UdtInfoInvalidError extends ApiError {
  // 固定错误参数（除 DETAIL 外）
  private static final int CODE = 1030;
  private static final int STATUS = 400;
  private static final String TITLE = "UDT info parameters invalid";
  private static final String HREF = "https://nervosnetwork.github.io/ckb-explorer/public/api_doc.html";

  // 带参数的构造器：初始化错误参数，支持动态 detail
  public UdtInfoInvalidError(String detail) {
    super(CODE, STATUS, TITLE, detail, HREF);
  }
}