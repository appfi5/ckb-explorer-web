package com.ckb.explorer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ckb.explorer.entity.BasicBlock;
import java.util.List;

public interface BasicBlockService extends IService<BasicBlock> {

  List<BasicBlock> getAllBasicBlocks();
}