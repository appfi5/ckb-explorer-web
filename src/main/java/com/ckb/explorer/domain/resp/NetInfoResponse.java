package com.ckb.explorer.domain.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 网络信息响应对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NetInfoResponse {

  private String type = "net_info";
  private String version;

}