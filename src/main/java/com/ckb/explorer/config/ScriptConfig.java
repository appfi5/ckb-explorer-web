package com.ckb.explorer.config;


import com.ckb.explorer.domain.resp.Script;
import com.ckb.explorer.domain.resp.ScriptResponse;
import lombok.Data;

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

    private List<CellDep> cellDeps;

  }

  public LockScript getLockScriptByCodeHash(String codeHash) {
    return lockScripts.stream()
        .filter(lockScript -> Objects.equals(lockScript.getCodeHash(), codeHash)).findFirst().get();
  }

  public List<TypeScript> getTypeScriptByCodeHash(String codeHash) {
    return typeScripts.stream()
        .filter(typeScript -> Objects.equals(typeScript.getCodeHash(), codeHash)).toList();
  }

  public Script getScriptByOutPoint(String txHash, Integer index) {
    var lockOpt = lockScripts.stream()
        .filter(typeScript -> typeScript.getCellDeps().stream().anyMatch(
            cellDep -> Objects.equals(cellDep.getTxHash(), txHash) &&
                cellDep.getIndex().intValue() == index.intValue())).findFirst();
    if (lockOpt.isPresent()) {
      var lock = lockOpt.orElse(null);
      return lock == null ? null
          : new Script(lock.getName(), lock.getCodeHash(), lock.getHashType(), true, false);
    }

    var typeOpt = typeScripts.stream()
        .filter(typeScript -> typeScript.getCellDeps().stream().anyMatch(
            cellDep -> Objects.equals(cellDep.getTxHash(), txHash) &&
                cellDep.getIndex().intValue() == index.intValue())).findFirst();
    if (typeOpt.isPresent()) {
      var type = typeOpt.orElse(null);
      return type == null ? null
          : new Script(type.getName(), type.getCodeHash(), type.getHashType(), false, true);
    }

    return null;
  }
}
