package com.ckb.explorer.common.dto;

import com.ckb.explorer.enums.ResultCode;
import java.io.Serializable;
import lombok.Data;

/**
 * @author
 * @date 2020/03/26
 */
@Data
public class ResponseInfo<T> implements Serializable {
  public static final int SUCCESS = 200;
  private static final int FAILURE = 500;

  private int code;
  private String message;

  private T data;

  public ResponseInfo() {
  }
  public ResponseInfo(int code, String msg) {
    this.code = code;
    this.message = msg;
  }

  public ResponseInfo(int code, String message, T data) {
    this.code = code;
    this.message = message;
    this.data = data;
  }

  public static <T> ResponseInfo SUCCESS() {
    return new ResponseInfo(SUCCESS, "success", null);
  }

  public static <T> ResponseInfo SUCCESS(String message) {
    return new ResponseInfo(SUCCESS, message, null);
  }

  public static <T> ResponseInfo SUCCESS(String message, T data) {
    return new ResponseInfo(SUCCESS, message, data);
  }

  public static <T> ResponseInfo SUCCESS(T data) {
    return new ResponseInfo(SUCCESS, "success", data);
  }

  public static <T> ResponseInfo FAILURE(T data) {
    return new ResponseInfo(FAILURE, "failure", data);
  }

  public static <T> ResponseInfo FAILURE(String message, T data) {
    return new ResponseInfo(FAILURE, message, data);
  }

  public static <T> ResponseInfo FAILURE(String message) {
    return new ResponseInfo(FAILURE, message, null);
  }

  public static <T> ResponseInfo FAILURE() {
    return new ResponseInfo(FAILURE, "failure", null);
  }

  public static <T> ResponseInfo FAILURE(int code, T data, String message) {
    return new ResponseInfo(code, message, data);
  }

  public ResponseInfo(ResultCode resultCode) {
    this.code = resultCode.getCode();
    this.message = resultCode.getMsg();
  }

  public ResponseInfo(ResultCode resultCode, String msg, T data) {
    this.code = resultCode.getCode();
    this.message = msg;
    this.data = data;
  }
}
