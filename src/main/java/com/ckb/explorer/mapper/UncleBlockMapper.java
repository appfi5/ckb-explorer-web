package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ckb.explorer.entity.TxAssociationHeaderDep;
import com.ckb.explorer.entity.UncleBlock;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface UncleBlockMapper extends BaseMapper<UncleBlock> {

}