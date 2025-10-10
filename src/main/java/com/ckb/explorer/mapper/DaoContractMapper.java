package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ckb.explorer.entity.DaoContract;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DaoContractMapper extends BaseMapper<DaoContract> {

  @Select("select * from dao_contracts where id = 1")
  DaoContract defaultContract();
}
