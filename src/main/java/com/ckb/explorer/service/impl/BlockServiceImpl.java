package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.entity.Block;
import com.ckb.explorer.mapper.BlockMapper;
import com.ckb.explorer.service.BlockService;
import java.util.Set;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * BlockService的实现类 提供区块相关的服务实现，包括分页查询
 */
@Service
public class BlockServiceImpl extends ServiceImpl<BlockMapper, Block> implements BlockService {

  private static final Set<String> VALID_SORT_FIELDS = Set.of(
      "block_number", "reward", "timestamp", "transactions_count"
  );

  /**
   * 分页查询区块列表
   *
   * @param pageNum  当前页码
   * @param pageSize 每页条数
   * @param sort     排序字段和方式，格式为"字段名.排序方式"，如"number.desc"
   * @return 分页结果
   */
  public Page<Block> getBlocksByPage(int pageNum, int pageSize, String sort) {
    // 创建分页对象
    Page<Block> page = new Page<>(pageNum, pageSize);
    // 创建查询条件
    LambdaQueryWrapper<Block> queryWrapper = new LambdaQueryWrapper<>();

    // 设置默认排序
    if (sort == null || sort.isEmpty()) {
      sort = "block_number.desc";
    }

    // 解析排序参数
    String[] sortParts = sort.split("\\.", 2);
    String orderBy = sortParts[0];
    String ascOrDesc = sortParts.length > 1 ? sortParts[1].toLowerCase() : "desc";

    // 字段映射
    orderBy = switch (orderBy) {
      case "height" -> "block_number";
      case "transactions" -> "transactions_count";
      default -> orderBy;
    };

    if (!VALID_SORT_FIELDS.contains(orderBy)) {
      // 对应原接口报404
      throw new IllegalArgumentException();
    }

    // 添加排序条件
    boolean isAsc = "asc".equals(ascOrDesc);
    switch (orderBy) {
      case "block_number":
        if (isAsc) {
          queryWrapper.orderByAsc(Block::getBlock_number);
        } else {
          queryWrapper.orderByDesc(Block::getBlock_number);
        }
        break;
      case "reward":
        if (isAsc) {
          queryWrapper.orderByAsc(Block::getReward);
        } else {
          queryWrapper.orderByDesc(Block::getReward);
        }
        break;
      case "timestamp":
        if (isAsc) {
          queryWrapper.orderByAsc(Block::getTimestamp);
        } else {
          queryWrapper.orderByDesc(Block::getTimestamp);
        }
        break;
      case "transactions_count":
        if (isAsc) {
          queryWrapper.orderByAsc(Block::getTransactions_count);
        } else {
          queryWrapper.orderByDesc(Block::getTransactions_count);
        }
        break;
    }

    if(!orderBy.equals("block_number")){
      // 始终按block_number降序排序作为第二排序条件
      queryWrapper.orderByDesc(Block::getBlock_number);
    }

    // 执行分页查询
    return baseMapper.selectPage(page, queryWrapper);
  }

}