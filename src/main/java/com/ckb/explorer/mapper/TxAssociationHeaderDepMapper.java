package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ckb.explorer.entity.TxAssociationCellDep;
import com.ckb.explorer.entity.TxAssociationHeaderDep;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface TxAssociationHeaderDepMapper extends BaseMapper<TxAssociationHeaderDep> {

}