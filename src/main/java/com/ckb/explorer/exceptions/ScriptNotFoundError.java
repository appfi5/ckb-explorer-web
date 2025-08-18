package com.ckb.explorer.exceptions;

/**
 * 脚本未找到异常（对应 Ruby 的 Api::V1::Exceptions::ScriptNotFoundError）
 */
public class ScriptNotFoundError extends ApiError {
  // 固定错误参数（与 Ruby 中 super 传入的参数完全一致）
  private static final int CODE = 1029;
  private static final int STATUS = 404;
  private static final String TITLE = "Script not found";
  private static final String DETAIL = "Script not found";
  private static final String HREF = "https://nervosnetwork.github.io/ckb-explorer/public/api_doc.html";

  // 无参构造器：初始化固定错误参数
  public ScriptNotFoundError() {
    super(CODE, STATUS, TITLE, DETAIL, HREF);
  }
}