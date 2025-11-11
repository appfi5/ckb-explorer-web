package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ckb.explorer.entity.Script;
import com.ckb.explorer.entity.TxAssociationCellDep;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface TxAssociationCellDepMapper extends BaseMapper<TxAssociationCellDep> {

}