package com.ckb.explorer.service.impl;

import com.ckb.explorer.entity.BasicBlock;
import com.ckb.explorer.repository.BasicBlockRepository;
import com.ckb.explorer.service.BasicBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BasicBlockServiceImpl implements BasicBlockService {

    private final BasicBlockRepository basicBlockRepository;

    @Autowired
    public BasicBlockServiceImpl(BasicBlockRepository basicBlockRepository) {
        this.basicBlockRepository = basicBlockRepository;
    }

    @Override
    public List<BasicBlock> getAllBasicBlocks() {
        return basicBlockRepository.findAll();
    }

    @Override
    public BasicBlock getBasicBlockById(Long id) {
        Optional<BasicBlock> optionalBasicBlock = basicBlockRepository.findById(id);
        return optionalBasicBlock.orElse(null);
    }

    @Override
    public List<BasicBlock> getBasicBlocksByNumberRange(Long startNumber, Long endNumber) {
        // 这里需要根据实际需求实现，可能需要在Repository中添加自定义查询方法
        // 暂时返回所有区块
        return basicBlockRepository.findAll();
    }

    @Override
    public BasicBlock saveBasicBlock(BasicBlock basicBlock) {
        return basicBlockRepository.save(basicBlock);
    }
}