package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.dto.CellInputDto;
import com.ckb.explorer.entity.Input;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface InputMapper extends BaseMapper<Input> {

  Page<CellInputDto> getDisplayInputs(Page page, @Param("transactionId") Long transactionId);

}