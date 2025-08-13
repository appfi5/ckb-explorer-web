package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ckb.explorer.entity.CkbTransaction;
import com.ckb.explorer.entity.Output;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface OutputMapper extends BaseMapper<Output> {

}