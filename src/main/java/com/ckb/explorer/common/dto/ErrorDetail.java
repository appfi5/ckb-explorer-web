package com.ckb.explorer.common.dto;

/**
 * 错误详情模型（独立类，用于直接返回列表）
 */
public class ErrorDetail {
  private final String title;
  private final String detail;
  private final int code;
  private final int status;

  // 构造器：接收单个 ApiError 的属性
  public ErrorDetail(int code, String title, String detail, int status) {
    this.code = code;
    this.title = title;
    this.detail = detail;
    this.status = status;
  }

  // Getter：供 JSON 序列化（Spring 会自动将 List 转为数组）
  public int getCode() { return code; }
  public String getTitle() { return title; }
  public String getDetail() { return detail; }
  public int getStatus() { return status; }
}