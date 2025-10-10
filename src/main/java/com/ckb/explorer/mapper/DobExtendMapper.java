package com.ckb.explorer.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.dto.NftTransfersDto;
import com.ckb.explorer.domain.resp.CollectionsResp;
import com.ckb.explorer.entity.DobExtend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author dell
 * @description 针对表【dob_extend】的数据库操作Mapper
 * @createDate 2025-09-29 13:30:00
 * @Entity com.ckb.explore.worker.entity.DobExtend
 */
@DS("risingwave")
@Mapper
public interface DobExtendMapper extends BaseMapper<DobExtend> {


    @Select("<script> \n" +
            "select dob.id,dob.name,dob.description,dob.tags,dob.block_timestamp,dob.creator,\n" +
            "(select count(*) from dob_output  dop where  exists (\n" +
            "select * from dob_code dc where dc.dob_code_script_id=dop.type_script_id and dc.dob_extend_id=dob.id) and dop.block_timestamp> #{oneDayAgo} ) as transaction_24h_count \n" +
            " ,\n" +
            " (select count(*) from (select count(*) from dob_live_cells  dlc\n" +
            " where exists (\n" +
            "select * from dob_code dc where \n" +
            "dc.dob_code_script_id=dlc.type_script_id and dc.dob_extend_id=dob.id) \n" +
            "group by dlc.lock_script_id )) as holders_count,\n" +
            "(select count(*) from dob_live_cells  dlc\n" +
            " where exists (\n" +
            "select * from dob_code dc where \n" +
            "dc.dob_code_script_id=dlc.type_script_id and dc.dob_extend_id=dob.id) \n" +
            ") as items_count \n" +
            " from dob_extend dob \n" +
            " where 1=1 \n"+
            " <if test=\" null != tags and tags !='' \"> \n" +
            "   and  tags @>  string_to_array(#{tags}, ',')::varchar[] \n"+
            " </if> \n"+
            " order by ${orderByStr} ${ascOrDesc} \n"+
            "</script>")
    Page<CollectionsResp> dobPage(Page page, @Param("oneDayAgo") Long oneDayAgo , @Param("orderByStr") String orderByStr,
                                  @Param("ascOrDesc") String ascOrDesc, @Param("tags") String tags);


    @Select("<script>" +
            "select dob.*,case when dob.is_spent=0 then \n" +
            "(select lock_script_id from dob_output  dof where dof.consumed_tx_hash = dob.tx_hash  limit 1)--寻找from 为null为铸造\n" +
            "  when dob.is_spent=1 then\n" +
            "  (select lock_script_id from dob_output dot where dot.tx_hash = dob.consumed_tx_hash limit 1 )--寻找 to 为null为销毁\n" +
            "  end as ft_lock_script_id\n" +
            "  from dob_output  dob where  exists (\n" +
            "select * from dob_code dc where dc.dob_code_script_id=dob.type_script_id and dc.dob_extend_id= \n" +
            "#{dobScriptId}) \n" +
            " <if test='null != txHash '>  \n" +
            "  and dob.txHash = #{txHash}\n" +
            " </if> \n" +
            " <if test='null != lockScriptId '>  \n" +
            "  and (dob.lock_script_id = #{lockScriptId} or ft_lock_script_id = #{lockScriptId} \n" +
            " </if> \n" +
            "</script>")
    Page<NftTransfersDto> transfersPage(Page page ,@Param("txHash") byte[] txHash,@Param("lockScriptId") Long lockScriptId);

}




