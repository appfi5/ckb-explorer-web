package com.ckb.explorer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ckb.explorer.entity.TypeScriptExtend;

public interface TypeScriptExtendService extends IService<TypeScriptExtend> {

  TypeScriptExtend getScriptId(Long scriptId);
}
