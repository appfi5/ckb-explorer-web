package com.ckb.explorer.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ckb.explorer.domain.dto.UdtAddressCountDto;
import com.ckb.explorer.entity.UdtHolderAllocations;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author dell
* @description 针对表【udt_holder_allocations】的数据库操作Mapper
* @createDate 2025-09-08 17:09:56
* @Entity com.ckb.explorer.entity.UdtHolderAllocations
*/
@DS("risingwave")
public interface UdtHolderAllocationsMapper extends BaseMapper<UdtHolderAllocations> {


    @Select("select sum(holder_count) from udt_holder_allocations where type_script_id = #{typeScriptId} ")
    Long selectHolderCountByTypeScriptId(@Param("typeScriptId") Long typeScriptId);

    List<UdtAddressCountDto> getAddressNumByScriptHashes(@Param("scriptHashes") List<byte[]> scriptHashes);

}




