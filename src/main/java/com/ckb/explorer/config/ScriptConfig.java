package com.ckb.explorer.config;


import com.ckb.explorer.domain.resp.ScriptResponse;
import com.ckb.explorer.enums.CellType;
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

    public Long typeScriptId;

    private String name;

    private String codeHash;

    private String hashType;

    private String args;

    private List<CellDep> cellDeps;

    private String symbol;

    private Integer decimal;

    private String scriptHash;

    private Integer cellType;

    private Integer version;
  }

  public LockScript getLockScriptByCodeHash(String codeHash) {
    return lockScripts.stream()
        .filter(lockScript -> Objects.equals(lockScript.getCodeHash(), codeHash)).findFirst().orElse(null);
  }

  public TypeScript getTypeScriptByCodeHash(String codeHash, String args) {
    List<TypeScript> typeScriptList=  typeScripts.stream()
        .filter(typeScript -> Objects.equals(typeScript.getCodeHash(), codeHash)).toList();
    if(typeScriptList.size() > 1 && !StringUtils.isEmpty(args)){
      // 优先返回匹配上args的
      var type = typeScriptList.stream().filter(typeScript -> Objects.equals(typeScript.getArgs(), args)).findFirst().orElse(null);
      if(type == null){
        // 降级返回匹配上codeHash且args为空的，比如基础类的合约
        return typeScriptList.stream().filter(typeScript -> Objects.equals(typeScript.getArgs(), null)).findFirst().orElse(null);
      }
      return type;
    }
    return typeScriptList.size() > 0 ? typeScriptList.get(0) : null;
  }

  public TypeScript getTypeScriptById(Long TypeScriptId) {

    if(TypeScriptId == null)
      return null;
    return typeScripts.stream()
        .filter(typeScript -> Objects.equals(typeScript.getTypeScriptId(), TypeScriptId)).findFirst().orElse(null);
  }

  public TypeScript getTypeScriptByCellType(Integer cellType, String codeHash) {
    List<TypeScript> typeScriptList=  typeScripts.stream()
            .filter(typeScript -> Objects.equals(typeScript.getCellType(), cellType)).toList();
    if(typeScriptList.size() > 1 && !org.springframework.util.StringUtils.isEmpty(codeHash) ){
      return typeScriptList.stream().filter(typeScript -> Objects.equals(typeScript.getCodeHash(), codeHash)).findFirst().orElse(null);
    }
    return typeScriptList.size() > 0 ? typeScriptList.get(0) : null;
  }


  public Integer cellType(ScriptConfig.TypeScript typeScript, String outputData) {
    if(typeScript==null){
      return CellType.NORMAL.getValue();
    }

    if(typeScript.getCellType()==null){
      return CellType.NORMAL.getValue();
    }

    if(CellType.NERVOS_DAO_DEPOSIT.getValue()==typeScript.getCellType()){
      if("0x0000000000000000".equals(outputData)){
        return CellType.NERVOS_DAO_DEPOSIT.getValue();
      }else {
        return CellType.NERVOS_DAO_WITHDRAWING.getValue();
      }
    }
    return typeScript.getCellType();
  }

}
