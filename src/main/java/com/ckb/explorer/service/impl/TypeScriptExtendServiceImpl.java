package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.entity.TypeScriptExtend;
import com.ckb.explorer.mapper.TypeScriptExtendMapper;
import com.ckb.explorer.service.TypeScriptExtendService;
import org.springframework.stereotype.Service;

@Service
public class TypeScriptExtendServiceImpl extends
    ServiceImpl<TypeScriptExtendMapper, TypeScriptExtend> implements
    TypeScriptExtendService {

  @Override
  public TypeScriptExtend getScriptId(Long scriptId) {
    return baseMapper.selectByTypeScriptId(scriptId);
  }
}
