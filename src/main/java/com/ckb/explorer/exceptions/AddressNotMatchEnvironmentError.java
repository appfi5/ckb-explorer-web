package com.ckb.explorer.exceptions;

/**
 * 地址与环境不匹配异常（对应 Ruby 的 Api::V1::Exceptions::AddressNotMatchEnvironmentError）
 */
public class AddressNotMatchEnvironmentError extends ApiError {
  // 固定错误参数（除 DETAIL 外）
  private static final int CODE = 1023;
  private static final int STATUS = 422;
  private static final String TITLE = "URI parameters is invalid";
  private static final String HREF = "https://nervosnetwork.github.io/ckb-explorer/public/api_doc.html";

  // 带参数的构造器：初始化错误参数，支持动态 detail
  public AddressNotMatchEnvironmentError(String ckbNetMode) {
    super(CODE, STATUS, TITLE, "This address is not the " + ckbNetMode + " address", HREF);
  }
}