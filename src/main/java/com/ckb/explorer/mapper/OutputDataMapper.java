package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ckb.explorer.entity.OutputData;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OutputDataMapper extends BaseMapper<OutputData> {

    @Select("<script>" +
            " select * from output_data where " +
            " output_id in  " +
            "      <foreach  item=\"item\" index=\"index\" collection=\"outputIds\" open=\"(\" separator=\",\" close=\")\">\n" +
            "        #{item}\n" +
            "      </foreach>" +
            "</script>")
    List<OutputData> selectByOutputIds(@Param("outputIds") List<Long> outputIds);
}
