package com.ckb.explorer.exceptions;

/**
 * 脚本哈希类型参数无效异常（对应 Ruby 的 Api::V1::Exceptions::ScriptHashTypeParamsInvalidError）
 */
public class ScriptHashTypeParamsInvalidError extends ApiError {
  // 固定错误参数（与 Ruby 中 super 传入的参数完全一致）
  private static final int CODE = 1028;
  private static final int STATUS = 404;
  private static final String TITLE = "URI parameters invalid";
  private static final String DETAIL = "hash type should be 'type'";
  private static final String HREF = "https://nervosnetwork.github.io/ckb-explorer/public/api_doc.html";

  // 无参构造器：初始化固定错误参数
  public ScriptHashTypeParamsInvalidError() {
    super(CODE, STATUS, TITLE, DETAIL, HREF);
  }
}