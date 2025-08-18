package com.ckb.explorer.exceptions;

/**
 * 脚本代码哈希参数无效异常（对应 Ruby 的 Api::V1::Exceptions::ScriptCodeHashParamsInvalidError）
 */
public class ScriptCodeHashParamsInvalidError extends ApiError {
  // 固定错误参数（与 Ruby 中 super 传入的参数完全一致）
  private static final int CODE = 1027;
  private static final int STATUS = 404;
  private static final String TITLE = "URI parameters invalid";
  private static final String DETAIL = "code hash should be start with 0x";
  private static final String HREF = "https://nervosnetwork.github.io/ckb-explorer/public/api_doc.html";

  // 无参构造器：初始化固定错误参数
  public ScriptCodeHashParamsInvalidError() {
    super(CODE, STATUS, TITLE, DETAIL, HREF);
  }
}