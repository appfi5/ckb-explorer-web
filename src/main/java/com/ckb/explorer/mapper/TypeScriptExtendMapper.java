package com.ckb.explorer.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ckb.explorer.entity.TypeScriptExtend;
import org.apache.ibatis.annotations.Mapper;

/**
 * TypeScriptExtendMapper 合约扩展接口
 * 用于操作type_script_extend表
 */
@Mapper
@DS("risingwave")
public interface TypeScriptExtendMapper extends BaseMapper<TypeScriptExtend> {

}