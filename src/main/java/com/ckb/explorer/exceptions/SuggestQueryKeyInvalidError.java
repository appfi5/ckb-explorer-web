package com.ckb.explorer.exceptions;

/**
 * 建议查询键无效异常（对应 Ruby 的 Api::V1::Exceptions::SuggestQueryKeyInvalidError）
 */
public class SuggestQueryKeyInvalidError extends ApiError {
  // 固定错误参数（与 Ruby 中 super 传入的参数完全一致）
  private static final int CODE = 1017;
  private static final int STATUS = 422;
  private static final String TITLE = "Query parameter is invalid";
  private static final String DETAIL = "Query parameter should be a block height, block hash, tx hash or address hash";
  private static final String HREF = "https://nervosnetwork.github.io/ckb-explorer/public/api_doc.html";

  // 无参构造器：初始化固定错误参数
  public SuggestQueryKeyInvalidError() {
    super(CODE, STATUS, TITLE, DETAIL, HREF);
  }
}