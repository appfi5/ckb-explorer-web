package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.entity.CkbTransaction;
import com.ckb.explorer.mapper.CkbTransactionMapper;
import com.ckb.explorer.service.CkbTransactionService;
import com.ckb.explorer.util.I18n;
import jakarta.annotation.Resource;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class CkbTransactionServiceImpl extends ServiceImpl<CkbTransactionMapper, CkbTransaction> implements
    CkbTransactionService {

  @Resource
  private I18n i18n;

  // 有效的排序字段
  private static final Set<String> VALID_SORT_FIELDS = new HashSet<String>() {
    {
      add("id");
      add("blockNumber");
      add("capacityInvolved");
    }
  };

  @Override
  public Page<CkbTransaction> getCkbTransactionsByPage(int pageNum, int pageSize, String sort) {
    // 解析排序参数
    String[] sortParts = sort.split("\\.", 2);
    String orderBy = sortParts[0];
    String ascOrDesc = sortParts.length > 1 ? sortParts[1].toLowerCase() : "desc";

    // 字段映射
    orderBy = switch (orderBy) {
      case "height" -> "blockNumber";
      case "capacity" -> "capacityInvolved";
      default -> orderBy;
    };

    // 验证排序字段
    if (!VALID_SORT_FIELDS.contains(orderBy)) {
      throw new ServerException(i18n.getMessage(I18nKey.SORT_ERROR_MESSAGE));
    }

    // 创建分页对象
    Page<CkbTransaction> pageResult = new Page<>(pageNum, pageSize);
    // 创建查询条件
    LambdaQueryWrapper<CkbTransaction> queryWrapper = new LambdaQueryWrapper<>();

    // 添加排序条件
    boolean isAsc = "asc".equals(ascOrDesc);
    switch (orderBy) {
      case "id":
        if (isAsc) {
          queryWrapper.orderByAsc(CkbTransaction::getId);
        } else {
          queryWrapper.orderByDesc(CkbTransaction::getId);
        }
        break;
      case "blockNumber":
        if (isAsc) {
          queryWrapper.orderByAsc(CkbTransaction::getBlockNumber);
        } else {
          queryWrapper.orderByDesc(CkbTransaction::getBlockNumber);
        }
        break;
        // TODO 字段待确定
//      case "capacityInvolved":
//        if (isAsc) {
//          queryWrapper.orderByAsc(CkbTransaction::getCapacityInvolved);
//        } else {
//          queryWrapper.orderByDesc(CkbTransaction::getCapacityInvolved);
//        }
//        break;
    }

    // 执行分页查询
    return baseMapper.selectPage(pageResult, queryWrapper);
  }

  @Override
  public CkbTransaction getTransactionByHash(String txHash) {
    return null;
  }
}
