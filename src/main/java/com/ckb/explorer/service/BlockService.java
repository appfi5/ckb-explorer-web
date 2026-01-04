package com.ckb.explorer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ckb.explorer.domain.dto.DaoBlockDto;
import com.ckb.explorer.domain.resp.BlockListResponse;
import com.ckb.explorer.domain.resp.BlockResponse;
import com.ckb.explorer.domain.resp.Last7DaysCkbNodeVersionResponse;
import com.ckb.explorer.entity.Block;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 区块服务接口
 */
public interface BlockService extends IService<Block> {

  List<BlockListResponse> getHomePageBlocks(int size);

  /**
   * 分页查询区块列表
   *
   * @param pageNum  当前页码
   * @param pageSize 每页条数
   * @param sort     排序字段和方式，格式为"字段名.排序方式"，如"number.desc"
   * @return 分页结果
   */
  Page<BlockListResponse> getBlocksByPage(int pageNum, int pageSize, String sort);

  BlockResponse getBlock(String id);

  Map<Long, byte[]> getBlockDaos(Set<Long> blockNumbers);

  Long getMaxBlockNumber();

  List<Last7DaysCkbNodeVersionResponse> getCkbNodeVersions();

  DaoBlockDto getDaoBlockByBlockNumber(Long blockNumber);
}