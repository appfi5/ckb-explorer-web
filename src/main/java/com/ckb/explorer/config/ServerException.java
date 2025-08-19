package com.ckb.explorer.config;


import com.ckb.explorer.enums.ResultCode;
import lombok.Getter;

public class ServerException extends RuntimeException {

  @Getter
  private final int code;
  @Getter
  private final String msg;
  @Getter
  private final String exception;

  public ServerException(int code, String msg) {
    super(msg);
    this.code = code;
    this.msg = msg;
    this.exception = msg;
  }

  public ServerException(String msg) {
    super(msg);
    this.code = 500;
    this.msg = msg;
    this.exception = msg;
  }

  public ServerException(String msg, String exception) {
    super(msg);
    this.code = 500;
    this.msg = msg;
    this.exception = exception;
  }

  public ServerException(ResultCode rc) {
    super(rc.getMsg());
    this.code = rc.getCode();
    this.msg = rc.getMsg();
    this.exception = rc.getMsg();
  }

  public ServerException(ResultCode rc, String msg) {
    super(msg);
    this.code = rc.getCode();
    this.msg = msg;
    this.exception = rc.getMsg();
  }

  public ServerException(ResultCode rc, String msg, String exception) {
    super(msg);
    this.code = rc.getCode();
    this.msg = msg;
    this.exception = exception;
  }
}
