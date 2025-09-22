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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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
    queryWrapper.orderByDesc(Block::getBlockNumber);

    var blocks = baseMapper.selectPage(page, queryWrapper);
    Map<Long, Block> afterBlockMap;
    List<Block> records = blocks.getRecords();
    if (!records.isEmpty()) {
      // 直接生成“原区块号+11”的列表，无需中间变量blockNumbers
      List<Long> afterNumbers = records.stream()
          .map(block -> block.getBlockNumber() + 11)
          .distinct() // 避免重复的区块号（若原分页有重复number，减少in查询的参数数量）
          .toList();
      LambdaQueryWrapper<Block> afterWrapper = new LambdaQueryWrapper<>();
      afterWrapper.in(Block::getBlockNumber, afterNumbers)
          .select(Block::getBlockNumber, Block::getReward); // 只查需要的字段（投影查询，减少IO）
      List<Block> afterBlocks = baseMapper.selectList(afterWrapper);

      afterBlockMap = afterBlocks.stream()
          .collect(Collectors.toMap(
              Block::getBlockNumber, // key：目标区块号（原number+11）
              block -> block,        // value：对应的区块对象
              (k1, k2) -> k1         // 若有重复key（理论上区块号唯一，此处为防御），取第一个
          ));
    } else {
      afterBlockMap = new HashMap<>(pageSize);
    }

    records.forEach(block -> {
      long targetBlockNumber = block.getBlockNumber() + 11;
      Block afterBlock = afterBlockMap.get(targetBlockNumber); // O(1)查询
      block.setReward(afterBlock != null ? afterBlock.getReward() : 0L);
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