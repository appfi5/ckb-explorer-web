package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.dto.CellOutputDto;
import com.ckb.explorer.entity.Output;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OutputMapper extends BaseMapper<Output> {

  Page<CellOutputDto> getCellbaseDisplayOutputs(Page page, @Param("transactionId") Long transactionId);

  Page<CellOutputDto> getNormalTxDisplayOutputs(Page page, @Param("transactionId") Long transactionId);

}