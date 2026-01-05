package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.dto.BlockDaoDto;
import com.ckb.explorer.domain.dto.DaoBlockDto;
import com.ckb.explorer.domain.dto.Last7DaysCkbNodeVersionDto;
import com.ckb.explorer.domain.resp.BlockListResponse;
import com.ckb.explorer.domain.resp.BlockResponse;
import com.ckb.explorer.domain.resp.Last7DaysCkbNodeVersionResponse;
import com.ckb.explorer.entity.Block;
import com.ckb.explorer.mapper.BlockMapper;
import com.ckb.explorer.mapstruct.BlockConvert;
import com.ckb.explorer.service.BlockService;
import com.ckb.explorer.util.CollectionUtils;
import com.ckb.explorer.util.I18n;
import com.ckb.explorer.util.QueryKeyUtils;
import com.ckb.explorer.util.TypeConversionUtil;
import jakarta.annotation.Resource;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
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
    Page<Block> page = new Page<>(pageNum, pageSize);
    page.setSearchCount(false);
    var total = baseMapper.getTotalCount();
    page.setTotal(total);
    // 创建分页对象
    if(pageNum <= 1){
      page = baseMapper.getPageBlocks(page);
    } else{
      var last = total - (pageNum - 1) * pageSize + 1;
      if(last < 0){
        return new Page<>(pageNum, pageSize, total);
      }
      List< Block> blocks = baseMapper.getLargePageBlocks(total - (pageNum - 1) * pageSize + 1 ,pageSize);
      page.setRecords(blocks);
    }

    Map<Long, Block> afterBlockMap;
    List<Block> records = page.getRecords();
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
    return BlockConvert.INSTANCE.toConvertPage(page);
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

  @Override
  public List<Last7DaysCkbNodeVersionResponse> getCkbNodeVersions() {
    Long from = Instant.now().minus(7, ChronoUnit.DAYS).toEpochMilli();
    List<Last7DaysCkbNodeVersionDto> list = baseMapper.getLast7DaysCkbNodeVersion(from);

    Map<String, Last7DaysCkbNodeVersionResponse> versionCountMap = new HashMap<>();

    for(Last7DaysCkbNodeVersionDto item : list){
      String version = TypeConversionUtil.minerMessageToVersion(item.getVersion());
      // 跳过空版本号
      if (version == null) {
        continue;
      }
      Last7DaysCkbNodeVersionResponse existingResponse = versionCountMap.get(version);
      if (existingResponse != null) {
        // 若已存在：累加计数（核心需求）
        existingResponse.setCount(existingResponse.getCount() + item.getCount());
      } else {
        // 若不存在：新建响应对象并放入Map
        Last7DaysCkbNodeVersionResponse newResponse = new Last7DaysCkbNodeVersionResponse(version, item.getCount());
        versionCountMap.put(version, newResponse);
      }
    }
    List<Last7DaysCkbNodeVersionResponse> resultList = new ArrayList<>(versionCountMap.values());
    resultList.sort(Comparator.comparing(
        Last7DaysCkbNodeVersionResponse::getVersion,
        (v1, v2) -> compareVersionStrings(v1, v2)
    ));
    return resultList;
  }

  @Override
  public DaoBlockDto getDaoBlockByBlockNumber(Long blockNumber) {
    return baseMapper.getDaoBlockByBlockNumber(blockNumber);
  }


  /**
   * 辅助方法：比较两个语义化版本字符串（x.y.z格式），实现自然排序
   * @param version1 第一个版本号
   * @param version2 第二个版本号
   * @return 负数：v1 < v2；0：v1 = v2；正数：v1 > v2
   */
  private int compareVersionStrings(String version1, String version2) {
    // 分割版本号为数字数组（按"."分割）
    String[] v1Segments = version1.split("\\.");
    String[] v2Segments = version2.split("\\.");
    // 取两个版本号的最大长度，避免遗漏分段（兼容x.y.z.w格式）
    int maxLength = Math.max(v1Segments.length, v2Segments.length);

    for (int i = 0; i < maxLength; i++) {
      // 若某个版本号分段不足，补0（如 0.200.0 和 0.200 视为相等）
      int v1Num = i < v1Segments.length ? safeParseInt(v1Segments[i]) : 0;
      int v2Num = i < v2Segments.length ? safeParseInt(v2Segments[i]) : 0;
      // 比较当前分段的数字大小
      if (v1Num != v2Num) {
        return Integer.compare(v1Num, v2Num);
      }
    }
    // 所有分段相等，版本号相同
    return 0;
  }

  /**
   * 安全转换字符串为整数，避免非数字版本号分段导致异常
   * @param str 版本号分段字符串
   * @return 转换后的整数，转换失败返回0
   */
  private int safeParseInt(String str) {
    try {
      return Integer.parseInt(str.trim());
    } catch (NumberFormatException e) {
      // 若分段不是数字（如 0.200.0-beta），按0处理
      return 0;
    }
  }
}