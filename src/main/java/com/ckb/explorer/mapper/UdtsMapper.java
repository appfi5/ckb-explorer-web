package com.ckb.explorer.mapper;

import com.ckb.explorer.entity.Udts;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
* @author dell
* @description 针对表【udts】的数据库操作Mapper
* @createDate 2025-09-05 11:01:18
* @Entity com.ckb.explorer.entity.Udts
*/
public interface UdtsMapper extends BaseMapper<Udts> {

  @Select("select * from udts where type_script_id = #{typeScriptId} and published = true")
  Udts getByTypeScriptId(@Param("typeScriptId") Long typeScriptId);
}




