package com.ckb.explorer.exceptions;

/**
 * 细胞输出数据大小超过限制异常（对应 Ruby 的 Api::V1::Exceptions::CellOutputDataSizeExceedsLimitError）
 */
public class CellOutputDataSizeExceedsLimitError extends ApiError {
  // 固定错误参数（与 Ruby 中 super 传入的参数完全一致）
  private static final int CODE = 1022;
  private static final int STATUS = 400;
  private static final String TITLE = "Output Data is Too Large";
  private static final String DETAIL = "You can download output data up to 1000 KB";
  private static final String HREF = "https://nervosnetwork.github.io/ckb-explorer/public/api_doc.html";

  // 无参构造器：初始化固定错误参数
  public CellOutputDataSizeExceedsLimitError() {
    super(CODE, STATUS, TITLE, DETAIL, HREF);
  }
}