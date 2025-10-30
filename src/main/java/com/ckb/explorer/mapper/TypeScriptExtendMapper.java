package com.ckb.explorer.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ckb.explorer.entity.TypeScriptExtend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * TypeScriptExtendMapper 合约扩展接口
 * 用于操作type_script_extend表
 */
@Mapper
@DS("risingwave")
public interface TypeScriptExtendMapper extends BaseMapper<TypeScriptExtend> {

  @Select("select * from type_script_extend where script_id = #{scriptId}")
  TypeScriptExtend selectByTypeScriptId(@Param("scriptId") Long scriptId);
}