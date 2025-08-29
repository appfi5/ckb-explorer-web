package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.resp.BlockResponse;
import com.ckb.explorer.entity.Block;
import com.ckb.explorer.mapper.BlockMapper;
import com.ckb.explorer.mapstruct.BlockConvert;
import com.ckb.explorer.service.BlockService;
import com.ckb.explorer.util.I18n;
import com.ckb.explorer.util.QueryKeyUtils;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Set;
import org.nervos.ckb.utils.Numeric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * BlockService的实现类 提供区块相关的服务实现，包括分页查询
 */
@Service
public class BlockServiceImpl extends ServiceImpl<BlockMapper, Block> implements BlockService {

  @Resource
  private I18n i18n;

  @Resource
  private QueryKeyUtils queryKeyUtils;

  private static final Set<String> VALID_SORT_FIELDS = Set.of(
      "blockNumber", "reward", "timestamp", "transactionsCount"
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

//    // 设置默认排序
//    if (sort == null || sort.isEmpty()) {
//      sort = "blockNumber.desc";
//    }
//
//    // 解析排序参数
//    String[] sortParts = sort.split("\\.", 2);
//    String orderBy = sortParts[0];
//    String ascOrDesc = sortParts.length > 1 ? sortParts[1].toLowerCase() : "desc";
//
//    // 字段映射
//    orderBy = switch (orderBy) {
//      case "height" -> "blockNumber";
//      case "transactions" -> "transactionsCount";
//      default -> orderBy;
//    };
//
//    if (!VALID_SORT_FIELDS.contains(orderBy)) {
//      throw new ServerException(i18n.getMessage(I18nKey.SORT_ERROR_MESSAGE));
//    }

    // 创建分页对象
    Page<Block> page = new Page<>(pageNum, pageSize);
    // 创建查询条件
    LambdaQueryWrapper<Block> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.orderByDesc(Block::getBlockNumber);
    // 添加排序条件
//    boolean isAsc = "asc".equals(ascOrDesc);
//    switch (orderBy) {
//      case "blockNumber":
//        if (isAsc) {
//          queryWrapper.orderByAsc(Block::getBlockNumber);
//        } else {
//          queryWrapper.orderByDesc(Block::getBlockNumber);
//        }
//        break;
//      case "reward":
//        if (isAsc) {
//          queryWrapper.orderByAsc(Block::getReward);
//        } else {
//          queryWrapper.orderByDesc(Block::getReward);
//        }
//        break;
//      case "timestamp":
//        if (isAsc) {
//          queryWrapper.orderByAsc(Block::getTimestamp);
//        } else {
//          queryWrapper.orderByDesc(Block::getTimestamp);
//        }
//        break;
//      case "transactionsCount":
//        if (isAsc) {
//          queryWrapper.orderByAsc(Block::getTransactionsCount);
//        } else {
//          queryWrapper.orderByDesc(Block::getTransactionsCount);
//        }
//        break;
//    }
//
//    if(!orderBy.equals("blockNumber")){
//      // 始终按blockNumber降序排序作为第二排序条件
//      queryWrapper.orderByDesc(Block::getBlockNumber);
//    }

    var blocks = baseMapper.selectPage(page, queryWrapper);
    List<Long> blockNumbers  = blocks.getRecords().stream().map(Block::getBlockNumber).toList();
    // 每个number加11
    var afterNumbers = blockNumbers.stream().map(number -> number+11).toList();
    // 查11个块之后的块的奖励
    var afterBlocks = baseMapper.selectList(new LambdaQueryWrapper<Block>().in(Block::getBlockNumber, afterNumbers));

    blocks.getRecords().forEach(block -> {
      var afterBlock = afterBlocks.stream().filter(block1 -> block1.getBlockNumber() == block.getBlockNumber()+11).findFirst().orElse(null);
      // 块奖励展示为11个块之后的奖励
      if(afterBlock != null){
        block.setReward(afterBlock.getReward());
      } else {
        block.setReward(0L);
      }
    });
    // 执行分页查询
    return blocks;
  }

  @Override
  public BlockResponse getBlock(String id) {
    Block block;
    if (queryKeyUtils.isValidHex(id)) {
      // 按区块哈希查询
      block = this.getOne(new LambdaQueryWrapper<Block>().eq(Block::getBlockHash, Numeric.hexStringToByteArray(id)));
    } else {
      // 按区块号查询
      block = this.getOne(new LambdaQueryWrapper<Block>().eq(Block::getBlockNumber, Long.parseLong(id)));
    }

    if (block == null) {
      throw new ServerException(I18nKey.BLOCK_NOT_FOUND_CODE, i18n.getMessage(I18nKey.BLOCK_NOT_FOUND_MESSAGE));
    }

    // 转换为响应对象
    var result = BlockConvert.INSTANCE.toConvertBlockResponse(block);

    var currentBlockNumber = result.getNumber();
    var afterBlock = baseMapper.selectOne(new LambdaQueryWrapper<Block>().eq(Block::getBlockNumber, currentBlockNumber+11));
    if(afterBlock == null){
      result.setMinerReward(0L);
      result.setRewardStatus("pending");
      result.setReceivedTxFeeStatus("pending");
    }else{
      result.setMinerReward(afterBlock.getReward());
      result.setRewardStatus("issued");
      result.setReceivedTxFeeStatus("calculated");
    }

    return result;
  }

}