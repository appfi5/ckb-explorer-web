package com.ckb.explorer.common.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * @author
 * @date 2020/03/26
 */
@Data
public class ResponseInfo<T> implements Serializable {
  protected T data;
  public ResponseInfo() {
  }
  public ResponseInfo(T data) {
    this.data = data;
  }
  public static <T> ResponseInfo<T> SUCCESS(T data) {
    return new ResponseInfo(data);
  }
}
