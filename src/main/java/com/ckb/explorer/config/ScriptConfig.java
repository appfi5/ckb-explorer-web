package com.ckb.explorer.config;


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


    @Data
    public static class LockScript{

        private String name;

        private String codeHash;

        private String hashType;

    }

    public LockScript getLockScriptByCodeHash(String codeHash){
        return lockScripts.stream().filter(lockScript -> Objects.equals(lockScript.getCodeHash(),codeHash)).findFirst().get();
    }
}
