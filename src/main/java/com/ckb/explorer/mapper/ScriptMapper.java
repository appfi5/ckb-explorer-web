package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ckb.explorer.entity.Script;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ScriptMapper extends BaseMapper<Script> {

    @Select("select * from script ts  where code_hash = #{codeHash} and ts.args = (select script_hash from script s where s.id = #{dobCodeScriptId}   )  ")
    Script findTypeBurnLock(@Param("codeHash") byte[] codeHash,@Param("dobCodeScriptId") Long dobCodeScriptId);

}