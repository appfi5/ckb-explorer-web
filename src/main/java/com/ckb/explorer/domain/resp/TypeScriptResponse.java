package com.ckb.explorer.domain.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TypeScript序列化器，用于将Script实体转换为前端需要的JSON格式
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeScriptResponse {

  private String type = "type_script";
  private String args;
  private String codeHash;
  private String hashType;
  private String verifiedScriptName;
  private String scriptHash;
}