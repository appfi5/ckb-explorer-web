package com.ckb.explorer.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.dto.PendingTransactionDto;
import com.ckb.explorer.domain.resp.PendingTransactionPageResponse;
import com.ckb.explorer.entity.CkbPendingTransaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
@DS("pending")
public interface CkbPendingTransactionMapper extends BaseMapper<CkbPendingTransaction> {

  @Select("SELECT '0x' || encode(tx_hash, 'hex') as transaction_hash, bytes + 4 as bytes, created_at / 1000 as created_at, created_at as create_timestamp FROM ckb_transaction WHERE status = 0 ORDER BY created_at DESC")
  Page<PendingTransactionPageResponse> getPagePendingTransactions(Page page);

  PendingTransactionDto selectPendingTransactionWithCellDeps(@Param("txHash") byte[] txHash);
}