package com.ckb.explorer.config;


import com.ckb.explorer.domain.resp.ScriptResponse;
import lombok.Data;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@ConfigurationProperties(prefix = "script")
@Component
@Data
public class ScriptConfig {


  private List<LockScript> lockScripts;

  private List<TypeScript> typeScripts;


  @Data
  public static class LockScript {

    private String name;

    private String codeHash;

    private String hashType;

    private List<CellDep> cellDeps;
  }

  @Data
  public static class CellDep {

    private String txHash;

    private Integer index;

    private String depType;

    private ScriptResponse type;
  }

  @Data
  public static class TypeScript {

    private String name;

    private String codeHash;

    private String hashType;

    private String args;

    private List<CellDep> cellDeps;

    private String symbol;

    private Integer decimal;

    private String scriptHash;

    private Boolean udt=false;

  }

  public LockScript getLockScriptByCodeHash(String codeHash) {
    return lockScripts.stream()
        .filter(lockScript -> Objects.equals(lockScript.getCodeHash(), codeHash)).findFirst().orElse(null);
  }

  public TypeScript getTypeScriptByCodeHash(String codeHash, String args) {
    List<TypeScript> typeScriptList=  typeScripts.stream()
        .filter(typeScript -> Objects.equals(typeScript.getCodeHash(), codeHash)).toList();
    if(typeScriptList.size() > 1 && !StringUtils.isEmpty(args)){
      return typeScriptList.stream().filter(typeScript -> Objects.equals(typeScript.getArgs(), args)).findFirst().orElse(null);
    }
    return typeScriptList.size() > 0 ? typeScriptList.get(0) : null;
  }

}
