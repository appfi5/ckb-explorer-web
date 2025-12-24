package com.ckb.explorer.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.dto.PendingCellInputDto;
import com.ckb.explorer.entity.PendingInput;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@DS("pending")
public interface PendingInputMapper extends BaseMapper<PendingInput> {

  Page<PendingCellInputDto> getDisplayInputs(Page page, @Param("txHash") byte[] txHash);
}