package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.resp.LiveCellsResponse;
import com.ckb.explorer.entity.LiveCells;
import java.util.Set;
import org.apache.ibatis.annotations.Param;

/**
* @author dell
* @description 针对表【live_cells】的数据库操作Mapper
* @createDate 2025-09-02 13:43:49
* @Entity com.ckb.explore.worker.entity.LiveCells
*/
public interface LiveCellsMapper extends BaseMapper<LiveCells> {


  Page<LiveCellsResponse> getLiveCellsByLockScriptIdWithTypeScriptId(Page page,  @Param("lockScriptId") Long lockScriptId,@Param("typeScriptId")  Long typeScriptId);

  Page<LiveCellsResponse> getOthersLiveCellsByLockScriptId(Page page,  @Param("lockScriptId") Long lockScriptId);
}




