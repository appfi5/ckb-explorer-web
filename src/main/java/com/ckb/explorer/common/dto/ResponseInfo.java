package com.ckb.explorer.common.dto;

import java.io.Serializable;

/**
 * @author
 * @date 2020/03/26
 */
public class ResponseInfo<T> implements Serializable {
  protected T data;
  public ResponseInfo() {
  }
  public ResponseInfo(T data) {
    this.data = data;
  }
  public static <T> ResponseInfo SUCCESS(T data) {
    return new ResponseInfo(data);
  }
}
