package com.ckb.explorer.exceptions;

/**
 * 区块哈希无效异常（对应 Ruby 的 Api::V1::Exceptions::BlockHashInvalidError）
 */
public class BlockHashInvalidError extends ApiError {
  // 固定错误参数（与 Ruby 中 super 传入的参数完全一致）
  private static final int CODE = 1011;
  private static final int STATUS = 422;
  private static final String TITLE = "URI parameters is invalid";
  private static final String DETAIL = "URI parameters should be a block hash";
  private static final String HREF = "https://nervosnetwork.github.io/ckb-explorer/public/api_doc.html";

  // 无参构造器：初始化固定错误参数
  public BlockHashInvalidError() {
    super(CODE, STATUS, TITLE, DETAIL, HREF);
  }
}