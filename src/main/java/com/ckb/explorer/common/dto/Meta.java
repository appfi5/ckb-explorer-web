package com.ckb.explorer.common.dto;

import lombok.Data;

@Data
public class Meta {

  private Long total;

  private Integer page_size;

  private Integer total_pages;
}
