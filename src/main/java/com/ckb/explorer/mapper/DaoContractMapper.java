package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ckb.explorer.entity.DaoContract;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DaoContractMapper extends BaseMapper<DaoContract> {

  // DaoContract defaultContract();
}
