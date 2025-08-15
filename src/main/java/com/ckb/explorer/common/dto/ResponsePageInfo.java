package com.ckb.explorer.common.dto;


import lombok.Data;

/**
 * @author
 * @date 2020/03/26
 */
@Data
public class ResponsePageInfo<T>  extends ResponseInfo<T>  {
  private Meta meta;
  public ResponsePageInfo() {
  }
  public ResponsePageInfo(T data,Meta meta) {
    this.data = data;
    this.meta = meta;
  }
  public static <T> ResponsePageInfo<T> SUCCESS(T data,Long total, Integer page_size, Integer total_pages) {
    Meta meta = new Meta();
    meta.setTotal(total);
    meta.setPage_size(page_size);
    meta.setTotal_pages(total_pages);
    return new ResponsePageInfo(data, meta);
  }
}
