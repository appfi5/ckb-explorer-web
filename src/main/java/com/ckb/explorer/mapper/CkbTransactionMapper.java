package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.dto.TransactionDto;
import com.ckb.explorer.domain.resp.AddressTransactionPageResponse;
import com.ckb.explorer.entity.CkbTransaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CkbTransactionMapper extends BaseMapper<CkbTransaction> {

  TransactionDto selectTransactionWithCellDeps(@Param("txHash") byte[] txHash);

  Page<AddressTransactionPageResponse> selectPageByAddressScriptId(Page page, @Param("orderByStr") String orderBy, @Param("ascOrDesc") String ascOrDesc, @Param("addressScriptId") Long addressScriptId);
}