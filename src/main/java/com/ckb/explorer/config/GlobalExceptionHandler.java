package com.ckb.explorer.config;

import com.ckb.explorer.common.dto.ErrorDetail;
import com.ckb.explorer.exceptions.ApiError;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器 用于捕获并处理项目中抛出的各类异常
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 处理IllegalArgumentException异常
   *
   * @param e 异常对象
   * @return 错误详情响应
   */
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(code = org.springframework.http.HttpStatus.NOT_FOUND)
  public void handleIllegalArgumentException(IllegalArgumentException e) {

  }

  /**
   * 处理NullPointerException异常
   *
   * @param e 异常对象
   * @return 错误详情响应
   */
  @ExceptionHandler(NullPointerException.class)
  public List<ErrorDetail> handleNullPointerException(NullPointerException e) {
    var list = new ArrayList<ErrorDetail>();
    list.add(new ErrorDetail(
        500,
        "Internal Server Error",
        "Null pointer exception occurred",
        500
    ));
    return list;
  }

  /**
   * 处理所有其他未明确指定的异常
   *
   * @param e 异常对象
   * @return 错误详情响应
   */
  @ExceptionHandler(Exception.class)
  public List<ErrorDetail> handleGenericException(Exception e) {
    var list = new ArrayList<ErrorDetail>();
    list.add(new ErrorDetail(
        500,
        "Internal Server Error",
        "An unexpected error occurred",
        500
    ));
    return list;
  }

  /**
   * 处理所有Api异常
   *
   * @param error 异常对象
   * @return 错误详情响应
   */
  @ExceptionHandler(ApiError.class)
  public List<ErrorDetail> handleGenericException(ApiError error) {
    var list = new ArrayList<ErrorDetail>();
    list.add(new ErrorDetail(
        error.getCode(),
        error.getTitle(),
        error.getDetail(),
        error.getStatus()
    ));
    return list;
  }
}