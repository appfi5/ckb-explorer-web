package com.ckb.explorer.exceptions;

/**
 * 统计信息名称无效异常（对应 Ruby 的 Api::V1::Exceptions::StatisticInfoNameInvalidError）
 */
public class StatisticInfoNameInvalidError extends ApiError {
  // 固定错误参数（与 Ruby 中 super 传入的参数完全一致）
  private static final int CODE = 1019;
  private static final int STATUS = 422;
  private static final String TITLE = "URI parameters is invalid";
  private static final String DETAIL = "Given statistic info name is invalid";
  private static final String HREF = "https://nervosnetwork.github.io/ckb-explorer/public/api_doc.html";

  // 无参构造器：初始化固定错误参数
  public StatisticInfoNameInvalidError() {
    super(CODE, STATUS, TITLE, DETAIL, HREF);
  }
}