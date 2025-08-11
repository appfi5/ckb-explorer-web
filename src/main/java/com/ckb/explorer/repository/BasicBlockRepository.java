package com.ckb.explorer.repository;

import com.ckb.explorer.entity.BasicBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasicBlockRepository extends JpaRepository<BasicBlock, Long> {
    // 可以添加自定义查询方法
}