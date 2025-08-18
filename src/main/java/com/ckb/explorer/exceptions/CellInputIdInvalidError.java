package com.ckb.explorer.exceptions;

/**
 * 细胞输入ID无效异常（对应 Ruby 的 Api::V1::Exceptions::CellInputIdInvalidError）
 */
public class CellInputIdInvalidError extends ApiError {
  // 固定错误参数（与 Ruby 中 super 传入的参数完全一致）
  private static final int CODE = 1013;
  private static final int STATUS = 422;
  private static final String TITLE = "URI parameters is invalid";
  private static final String DETAIL = "URI parameters should be a integer";
  private static final String HREF = "https://nervosnetwork.github.io/ckb-explorer/public/api_doc.html";

  // 无参构造器：初始化固定错误参数
  public CellInputIdInvalidError() {
    super(CODE, STATUS, TITLE, DETAIL, HREF);
  }
}