package com.ckb.explorer.domain.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Since信息的内部类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SinceResponse {


  /**
   * 原始since值（十六进制）
   */
  private String raw;

  /**
   * 中值时间戳
   */
  private Long medianTimestamp; // TODO 原逻辑是同步时调用API get_block_median_time 落表，页面不展示，考虑不要

}
