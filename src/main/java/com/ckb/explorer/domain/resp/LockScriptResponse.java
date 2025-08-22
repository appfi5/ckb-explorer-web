package com.ckb.explorer.domain.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * LockScript序列化器，用于将Script实体转换为前端需要的JSON格式
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LockScriptResponse {

    private String args;
    private String codeHash;
    private String hashType;
    private String verifiedScriptName;
    private List<String> tags;
}