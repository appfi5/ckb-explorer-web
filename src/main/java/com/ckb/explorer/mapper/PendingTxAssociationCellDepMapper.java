package com.ckb.explorer.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ckb.explorer.entity.PendingTxAssociationCellDep;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DS("pending")
public interface PendingTxAssociationCellDepMapper extends BaseMapper<PendingTxAssociationCellDep> {

}