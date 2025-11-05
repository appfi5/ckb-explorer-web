package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ckb.explorer.entity.OutputData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OutputDataMapper extends BaseMapper<OutputData> {

  @Select("select * from output_data where output_id = #{outputId}")
  OutputData selectByOutputId(Long outputId);
}
