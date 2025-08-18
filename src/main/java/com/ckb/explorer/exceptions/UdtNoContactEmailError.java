package com.ckb.explorer.exceptions;

/**
 * UDT无联系邮箱异常（对应 Ruby 的 Api::V1::Exceptions::UdtNoContactEmailError）
 */
public class UdtNoContactEmailError extends ApiError {
  // 固定错误参数（与 Ruby 中 super 传入的参数完全一致）
  private static final int CODE = 1033;
  private static final int STATUS = 400;
  private static final String TITLE = "UDT has no contact email";
  private static final String DETAIL = "";
  private static final String HREF = "https://nervosnetwork.github.io/ckb-explorer/public/api_doc.html";

  // 无参构造器：初始化固定错误参数
  public UdtNoContactEmailError() {
    super(CODE, STATUS, TITLE, DETAIL, HREF);
  }
}