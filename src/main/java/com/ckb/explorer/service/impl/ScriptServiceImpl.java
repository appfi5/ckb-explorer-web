package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.entity.Script;
import com.ckb.explorer.mapper.ScriptMapper;
import com.ckb.explorer.service.ScriptService;
import org.springframework.stereotype.Service;

@Service
public class ScriptServiceImpl extends ServiceImpl<ScriptMapper, Script> implements
    ScriptService {

}
