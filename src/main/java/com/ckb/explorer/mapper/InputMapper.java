package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.dto.CellInputDto;
import com.ckb.explorer.entity.Input;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface InputMapper extends BaseMapper<Input> {

  Page<CellInputDto> getDisplayInputs(Page page, @Param("transactionId") Long transactionId);

  List<CellInputDto> getDisplayInputs(@Param("transactionId") Long transactionId);

  List<CellInputDto> getNormalDisplayInputsByTransactionIds( @Param("transactionIds") List<Long> transactionIds, @Param("size")int size);

  List<CellInputDto> getDaoDisplayInputsByTransactionIds( @Param("transactionIds") List<Long> transactionIds, @Param("size")int size, @Param("typeScriptId") Long typeScriptId);
}