package com.ckb.explorer.exceptions;

/**
 * 合约未找到异常（对应 Ruby 的 Api::V1::Exceptions::ContractNotFoundError）
 */
public class ContractNotFoundError extends ApiError {
  // 固定错误参数（与 Ruby 中 super 传入的参数完全一致）
  private static final int CODE = 1021;
  private static final int STATUS = 404;
  private static final String TITLE = "Contract Not Found";
  private static final String DETAIL = "No contract records found by given contract name";
  private static final String HREF = "https://nervosnetwork.github.io/ckb-explorer/public/api_doc.html";

  // 无参构造器：初始化固定错误参数
  public ContractNotFoundError() {
    super(CODE, STATUS, TITLE, DETAIL, HREF);
  }
}