package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.entity.Output;
import com.ckb.explorer.mapper.OutputMapper;
import com.ckb.explorer.service.OutputService;
import org.springframework.stereotype.Service;

@Service
public class OutputServiceImpl extends ServiceImpl<OutputMapper, Output> implements
    OutputService {

  @Override
  public Long countAddressTransactions(Long scriptId) {
    return baseMapper.countAddressTransactions(scriptId);
  }
}
