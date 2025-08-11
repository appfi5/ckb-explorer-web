package com.ckb.explorer.service;

import com.ckb.explorer.entity.BasicBlock;
import java.util.List;

public interface BasicBlockService {
    List<BasicBlock> getAllBasicBlocks();
    BasicBlock getBasicBlockById(Long id);
    List<BasicBlock> getBasicBlocksByNumberRange(Long startNumber, Long endNumber);
    BasicBlock saveBasicBlock(BasicBlock basicBlock);
}