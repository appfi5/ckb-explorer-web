package com.ckb.explorer.config;

import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.enums.ResultCode;
import com.ckb.explorer.util.I18n;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局异常处理器 用于捕获并处理项目中抛出的各类异常
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @Resource
  private I18n i18n;

  @ExceptionHandler(NoHandlerFoundException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseInfo<Error> handleError404(HttpServletRequest request, NoHandlerFoundException e) {
    return new ResponseInfo<>(ResultCode.NOT_FOUND, "Request URL: [" + request.getRequestURI() + "] Not Found!",null);
  }

  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseInfo<Error> handleError404(HttpRequestMethodNotSupportedException e) {
    log.error("exception.", e);
    return new ResponseInfo<>(ResultCode.NOT_FOUND, e.getMessage(),null);
  }

  /**
   * 处理IllegalArgumentException异常
   *
   * @param e 异常对象
   * @return 错误详情响应
   */
  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseInfo<Error> illegalArgumentException(IllegalArgumentException e) {
    log.error("exception.", e);
    return new ResponseInfo<>(ResultCode.PARAM_INVALID, e.getMessage(), null);
  }

  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseInfo<Error> serverException(ServerException e) {
    log.error("ServerException: " + e.getMessage());
    return new ResponseInfo<>(e.getCode(),i18n.getMessage(e.getMessage()),null);
  }

  @ExceptionHandler(WarnException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  public ResponseInfo<String> warnException(WarnException e) {
    log.error("WarnException: " + e.getMessage());
    return new ResponseInfo<>(e.getCode(),i18n.getMessage(e.getMessage()),null);
  }

  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseInfo<Error> handleBindException(BindException e) {
    return new ResponseInfo<>(ResultCode.PARAM_INVALID.getCode(), errors(e.getBindingResult().getFieldErrors()));
  }


  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseInfo<Error> handleBodyValidException(MethodArgumentNotValidException exception) {
    return new ResponseInfo<>(ResultCode.PARAM_INVALID.getCode(), errors(exception.getBindingResult().getFieldErrors()));
  }

  private String errors(List<FieldError> fieldErrors){
    log.warn("参数绑定异常,ex = {}", fieldErrors);
    StringBuilder message = new StringBuilder();

    //将其组成键值对的形式存入map
    for (FieldError fieldError : fieldErrors) {
      Object[] args = fieldError.getArguments();
      if(null!=args) {
        String[] strs = new String[args.length];
        for(int i=0;i<args.length;i++) {
          if (args[i] instanceof DefaultMessageSourceResolvable) {
            String msg = ((DefaultMessageSourceResolvable) args[i]).getDefaultMessage();
            if(null!=msg) {
              msg = msg.replaceAll("\\[\\d\\]\\.", "").replaceAll("\\.+", "");
            }
            strs[i] = i18n.getMessage(msg);
          }
        }
        message.append(i18n.getMessage(fieldError.getDefaultMessage(),strs) + ",");
      }
    }
    message.deleteCharAt(message.length()-1);
    return message.toString();
  }

  @ExceptionHandler(Exception.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseInfo<Error> exception(Exception exp) {
    log.error("Exception.", exp);
    return new ResponseInfo<>(ResultCode.SERVER_ERROR.getCode(), i18n.getMessage(exp.getMessage()));
  }
}