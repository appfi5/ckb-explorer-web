package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.dto.CellOutputDto;
import com.ckb.explorer.entity.Output;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OutputMapper extends BaseMapper<Output> {

  List<CellOutputDto> getCellbaseDisplayOutputs( @Param("transactionId") Long transactionId);

  Page<CellOutputDto> getCellbaseDisplayOutputs(Page page, @Param("transactionId") Long transactionId);

  List<CellOutputDto> getNormalTxDisplayOutputs(@Param("transactionId") Long transactionId);

  Page<CellOutputDto> getNormalTxDisplayOutputs(Page page, @Param("transactionId") Long transactionId);

  List<CellOutputDto> getCellbaseDisplayOutputsByTransactionIds( @Param("transactionIds") List<Long> transactionIds);

  List<CellOutputDto> getNormalTxDisplayOutputsByTransactionIds(@Param("transactionIds") List<Long> transactionIds, @Param("size")int size);

  Long countAddressTransactions(@Param("lockScriptId") Long lockScriptId);

  List<CellOutputDto> getDaoDisplayOutputsByTransactionIds(@Param("transactionIds") List<Long> transactionIds, @Param("size")int size, @Param("typeScriptId") Long typeScriptId);

  @Select("select * from output where lock_script_id = #{lockScriptId} limit 1")
  Output findByLockScriptIdOutput(@Param("lockScriptId") Long lockScriptId);
}