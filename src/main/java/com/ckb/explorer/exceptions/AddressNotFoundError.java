package com.ckb.explorer.exceptions;

/**
 * 地址未找到异常（对应 Ruby 的 Api::V1::Exceptions::AddressNotFoundError）
 */
public class AddressNotFoundError extends ApiError {
  // 固定错误参数（与 Ruby 中 super 传入的参数完全一致）
  private static final int CODE = 1010;
  private static final int STATUS = 404;
  private static final String TITLE = "Address Not Found";
  private static final String DETAIL = "No address found by given address hash or lock hash";
  private static final String HREF = "https://nervosnetwork.github.io/ckb-explorer/public/api_doc.html";

  // 无参构造器：初始化固定错误参数
  public AddressNotFoundError() {
    super(CODE, STATUS, TITLE, DETAIL, HREF);
  }
}