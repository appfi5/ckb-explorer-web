package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ckb.explorer.domain.dto.TransactionDto;
import com.ckb.explorer.entity.CkbTransaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CkbTransactionMapper extends BaseMapper<CkbTransaction> {

  TransactionDto selectTransactionWithCellDeps(@Param("txHash") byte[] txHash);
}