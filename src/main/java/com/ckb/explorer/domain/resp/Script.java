package com.ckb.explorer.domain.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Script {

  private String name; // TODO 目前没有

  private String codeHash;//

  private String hashType;

  private Boolean isLockScript; // TODO 不一定有，待确认

  private Boolean isTypeScript;// TODO 不一定有，待确认
}
