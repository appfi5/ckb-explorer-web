package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.dto.BlockDaoDto;
import com.ckb.explorer.domain.dto.EpochDto;
import com.ckb.explorer.domain.dto.Last7DaysCkbNodeVersionDto;
import com.ckb.explorer.entity.Block;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BlockMapper extends BaseMapper<Block> {

  @Select("SELECT id, miner_script, block_number,timestamp,reward,transactions_count,live_cell_changes FROM block ORDER BY block_number DESC LIMIT #{size}")
  List<Block> getHomePageBlocks(@Param("size") int size);

  @Select("SELECT id, miner_script, block_number,timestamp,reward,transactions_count,live_cell_changes FROM block ORDER BY block_number DESC")
  Page<Block> getPageBlocks(Page page);

  @Select("SELECT id, miner_script, block_number,timestamp,reward,transactions_count,live_cell_changes FROM block WHERE id < #{lastId} ORDER BY block_number DESC LIMIT #{pageSize}")
  List<Block> getLargePageBlocks(@Param("lastId") Long lastId, @Param("pageSize") int pageSize);

  @Select({"<script>",
      "SELECT block_number, reward FROM block",
      "<where>",
        "<if test='afterNumbers != null and afterNumbers.size() > 0'>",
          "block_number IN",
          "<foreach collection='afterNumbers' item='number' open='(' separator=',' close=')'>",
            "#{number}",
          "</foreach>",
        "</if>",
      "</where>",
      "</script>"})
  List<Block> getAfterReward(@Param("afterNumbers") List<Long> afterNumbers);

  @Select("SELECT epoch_number as number, block_number - start_number as index,  epoch_length as length  FROM block ORDER BY block_number DESC LIMIT 1")
  EpochDto getRecentEpoch();

  List<BlockDaoDto> getBlockDaos(@Param("blockNumbers") List<Long> blockNumbers);

  @Select("select max(block_number) from block")
  Long getMaxBlockNumber();

  @Select("SELECT MAX(id) FROM block")
  Long getTotalCount();

  @Select("select miner_message as version, count(*) from block where timestamp >= #{from} group by miner_message order by 1 asc")
  List<Last7DaysCkbNodeVersionDto> getLast7DaysCkbNodeVersion(@Param("from") Long  from);
 }