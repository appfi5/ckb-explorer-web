package com.ckb.explorer.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.dto.DaoCellDto;
import com.ckb.explorer.domain.dto.DaoDepositorDto;
import com.ckb.explorer.entity.DepositCell;
import java.math.BigInteger;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @description 针对表【deposit_cell】的数据库操作Mapper
 * @Entity com.ckb.explore.worker.entity.DepositCell
 */
@DS("risingwave")
public interface DepositCellMapper extends BaseMapper<DepositCell> {

  Page<byte[]> getTxHashPage(Page page, @Param("txHash") byte[] txHash, @Param("lockScriptId") Long lockScriptId);

  List<DaoDepositorDto> getTopDaoDepositors();

  BigInteger getDepositByLockScriptId(@Param("lockScriptId")Long lockScriptId);

  List<DaoCellDto> getUnConsumedCellsByLockScriptId(@Param("lockScriptId")Long lockScriptId);
}