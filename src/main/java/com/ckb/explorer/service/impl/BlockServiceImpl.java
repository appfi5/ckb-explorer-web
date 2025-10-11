package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.dto.BlockDaoDto;
import com.ckb.explorer.domain.resp.BlockListResponse;
import com.ckb.explorer.domain.resp.BlockResponse;
import com.ckb.explorer.entity.Block;
import com.ckb.explorer.mapper.BlockMapper;
import com.ckb.explorer.mapstruct.BlockConvert;
import com.ckb.explorer.service.BlockService;
import com.ckb.explorer.util.CollectionUtils;
import com.ckb.explorer.util.I18n;
import com.ckb.explorer.util.QueryKeyUtils;
import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.nervos.ckb.utils.Numeric;
import org.springframework.stereotype.Service;

/**
 * BlockService的实现类 提供区块相关的服务实现，包括分页查询
 */
@Slf4j
@Service
public class BlockServiceImpl extends ServiceImpl<BlockMapper, Block> implements BlockService {

  @Resource
  private I18n i18n;

  @Resource
  private QueryKeyUtils queryKeyUtils;

  @Override
  public List<BlockListResponse> getHomePageBlocks(int size) {
    long start = System.currentTimeMillis();
    var blocks = baseMapper.getHomePageBlocks(size);
    Map<Long, Block> afterBlockMap;
    if (!blocks.isEmpty()) {
      // 直接生成“原区块号+11”的列表，无需中间变量blockNumbers
      List<Long> afterNumbers = blocks.stream()
          .map(block -> block.getBlockNumber() + 11)
          .distinct() // 避免重复的区块号（若原分页有重复number，减少in查询的参数数量）
          .toList();
      if (!afterNumbers.isEmpty()) {
        List<Block> afterBlocks = baseMapper.getAfterReward(afterNumbers);
        afterBlockMap = afterBlocks.stream()
            .collect(Collectors.toMap(
                Block::getBlockNumber, // key：目标区块号（原number+11）
                block -> block,        // value：对应的区块对象
                (k1, k2) -> k1         // 若有重复key（理论上区块号唯一，此处为防御），取第一个
            ));
      } else {
        afterBlockMap = new HashMap<>(size);
      }
      blocks.forEach(block -> {
        long targetBlockNumber = block.getBlockNumber() + 11;
        Block afterBlock = afterBlockMap.get(targetBlockNumber); // O(1)查询
        block.setReward(afterBlock != null ? afterBlock.getReward() : 0L);
      });
    }
    log.info("查询首页区块列表耗时：{}ms", System.currentTimeMillis() - start);
    return BlockConvert.INSTANCE.toConvertList(blocks);
  }

  /**
   * 分页查询区块列表
   *
   * @param pageNum  当前页码
   * @param pageSize 每页条数
   * @param sort     排序字段和方式，格式为"字段名.排序方式"，如"number.desc"
   * @return 分页结果
   */
  public Page<BlockListResponse> getBlocksByPage(int pageNum, int pageSize, String sort) {
    long start = System.currentTimeMillis();
    // 创建分页对象
    Page<Block> page = new Page<>(pageNum, pageSize);
    var blocks = baseMapper.getPageBlocks(page);
    Map<Long, Block> afterBlockMap;
    List<Block> records = blocks.getRecords();
    if (!records.isEmpty()) {
      // 直接生成“原区块号+11”的列表，无需中间变量blockNumbers
      List<Long> afterNumbers = records.stream()
          .map(block -> block.getBlockNumber() + 11)
          .distinct() // 避免重复的区块号（若原分页有重复number，减少in查询的参数数量）
          .toList();
      if (!afterNumbers.isEmpty()) {
        List<Block> afterBlocks = baseMapper.getAfterReward(afterNumbers);
        afterBlockMap = afterBlocks.stream()
            .collect(Collectors.toMap(
                Block::getBlockNumber, // key：目标区块号（原number+11）
                block -> block,        // value：对应的区块对象
                (k1, k2) -> k1         // 若有重复key（理论上区块号唯一，此处为防御），取第一个
            ));
      } else {
        afterBlockMap = new HashMap<>(pageSize);
      }

    } else {
      afterBlockMap = new HashMap<>(pageSize);
    }

    records.forEach(block -> {
      long targetBlockNumber = block.getBlockNumber() + 11;
      Block afterBlock = afterBlockMap.get(targetBlockNumber); // O(1)查询
      block.setReward(afterBlock != null ? afterBlock.getReward() : 0L);
    });
    log.info("查询区块列表耗时：{}ms", System.currentTimeMillis() - start);
    // 执行分页查询
    return BlockConvert.INSTANCE.toConvertPage(blocks);
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

  @Override
  public Map<Long, byte[]> getBlockDaos(Set<Long> blockNumbers) {
    List<List<Long>> batches = CollectionUtils.splitIntoBatches(blockNumbers, 1000);
    Map<Long, byte[]> blockDaos = new HashMap<>(blockNumbers.size()); // 预分配容量
    // 分批查询并合并结果
    for (List<Long> batch : batches) {
      List<BlockDaoDto> batchResult = baseMapper.getBlockDaos(batch);
      if(!batchResult.isEmpty()){
        blockDaos.putAll(batchResult.stream()
            .collect(Collectors.toMap(
                BlockDaoDto::getBlockNumber, // key：blockNumber
                BlockDaoDto::getDao,         // value：dao字段
                (existingValue, newValue) -> newValue, // 若key重复，保留后者
                HashMap::new                 // 指定Map实现（可选，默认是HashMap）
            )));
      }
    }
    return blockDaos;
  }

  @Override
  public Long getMaxBlockNumber() {
    return baseMapper.getMaxBlockNumber();
  }

}