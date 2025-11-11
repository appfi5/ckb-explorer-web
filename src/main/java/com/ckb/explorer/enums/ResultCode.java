package com.ckb.explorer.enums;

public enum ResultCode {
  SUCCESS(200, "success"),
  UNAUTHORIZED(401, "no login"),
  FORBIDDEN(403, "no permission"),
  PARAM_INVALID(400, "invalid param"),
  NOT_FOUND(404, "not found"),
  USER_LOCK(433, "the user has been locked, please contact the administrator"),
  USER_NOT_REST_PASSWORD(434, "please reset the password with mobile phone number first"),
  SERVER_ERROR(500, "server exception");

  int code;
  String msg;

  private ResultCode(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public int getCode() {
    return this.code;
  }

  public String getMsg() {
    return this.msg;
  }
}
