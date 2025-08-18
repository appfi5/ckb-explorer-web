package com.ckb.explorer.exceptions;

/**
 * 细胞输入未找到异常（对应 Ruby 的 Api::V1::Exceptions::CellInputNotFoundError）
 */
public class CellInputNotFoundError extends ApiError {
  // 固定错误参数（与 Ruby 中 super 传入的参数完全一致）
  private static final int CODE = 1014;
  private static final int STATUS = 404;
  private static final String TITLE = "Cell Input Not Found";
  private static final String DETAIL = "No cell input records found by given id";
  private static final String HREF = "https://nervosnetwork.github.io/ckb-explorer/public/api_doc.html";

  // 无参构造器：初始化固定错误参数
  public CellInputNotFoundError() {
    super(CODE, STATUS, TITLE, DETAIL, HREF);
  }
}