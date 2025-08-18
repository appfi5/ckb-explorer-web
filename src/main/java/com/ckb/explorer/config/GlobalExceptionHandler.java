package com.ckb.explorer.config;

import com.ckb.explorer.common.dto.ErrorDetail;
import com.ckb.explorer.exceptions.ApiError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 用于捕获并处理项目中抛出的各类异常
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 处理IllegalArgumentException异常
     * @param e 异常对象
     * @return 错误详情响应
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(code = org.springframework.http.HttpStatus.NOT_FOUND)
    public void handleIllegalArgumentException(IllegalArgumentException e) {

    }
    
    /**
     * 处理NullPointerException异常
     * @param e 异常对象
     * @return 错误详情响应
     */
    @ExceptionHandler(NullPointerException.class)
    public ErrorDetail handleNullPointerException(NullPointerException e) {
        return new ErrorDetail(
            500, 
            "Internal Server Error", 
            e.getMessage() != null ? e.getMessage() : "Null pointer exception occurred",
            500
        );
    }
    
    /**
     * 处理所有其他未明确指定的异常
     * @param e 异常对象
     * @return 错误详情响应
     */
    @ExceptionHandler(Exception.class)
    public ErrorDetail handleGenericException(Exception e) {
        return new ErrorDetail(
            500, 
            "Internal Server Error", 
            "An unexpected error occurred",
            500
        );
    }

  /**
   * 处理所有Api异常
   * @param error 异常对象
   * @return 错误详情响应
   */
  @ExceptionHandler(ApiError.class)
  public ErrorDetail handleGenericException(ApiError error) {
    return new ErrorDetail(
        error.getCode(),
        error.getTitle(),
        error.getDetail(),
        error.getStatus()
    );
  }
}