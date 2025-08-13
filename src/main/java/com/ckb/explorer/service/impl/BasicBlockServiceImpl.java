package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.entity.BasicBlock;
import com.ckb.explorer.mapper.BasicBlockMapper;
import com.ckb.explorer.service.BasicBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BasicBlockServiceImpl extends ServiceImpl<BasicBlockMapper, BasicBlock> implements BasicBlockService {

  @Autowired
  private BasicBlockMapper basicBlockMapper;


  @Override
  public List<BasicBlock> getAllBasicBlocks() {
    LambdaQueryWrapper<BasicBlock> queryWrapper = new LambdaQueryWrapper<>();
    return basicBlockMapper.selectList(queryWrapper);
  }
}