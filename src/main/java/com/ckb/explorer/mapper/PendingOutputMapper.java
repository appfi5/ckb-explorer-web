package com.ckb.explorer.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.dto.PendingCellOutputDto;
import com.ckb.explorer.entity.PendingOutput;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@DS("pending")
public interface PendingOutputMapper extends BaseMapper<PendingOutput> {

  Page<PendingCellOutputDto> getDisplayOutputs(Page page, @Param("txHash") byte[] txHash);
}