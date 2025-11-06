package com.ckb.explorer.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.CollectionsDto;
import com.ckb.explorer.domain.dto.AccountNftDto;
import com.ckb.explorer.domain.dto.NftHolderDto;
import com.ckb.explorer.domain.dto.NftItemDto;
import com.ckb.explorer.domain.dto.NftTransfersDto;
import com.ckb.explorer.domain.resp.NftCollectionResponse;
import com.ckb.explorer.domain.resp.NftResponse;
import com.ckb.explorer.entity.DobExtend;
import java.util.List;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.ArrayTypeHandler;

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
            "select dob.*,\n" +
            "(select count(*) from dob_output  dop where  exists (\n" +
            "select * from dob_code dc where dc.dob_code_script_id=dop.type_script_id and dc.dob_extend_id=dob.id) and dop.block_timestamp> #{oneDayAgo} ) as h24_ckb_transactions_count \n" +
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
    @Results({
             // 对数组类型指定自定义 TypeHandler
            @Result(column = "tags", property = "tags", typeHandler = ArrayTypeHandler.class)
    })
    Page<CollectionsDto> dobPage(Page page, @Param("oneDayAgo") Long oneDayAgo , @Param("orderByStr") String orderByStr,
                                  @Param("ascOrDesc") String ascOrDesc, @Param("tags") String tags);


    @Select( "select dob.*,\n" +
            "(select max(dobo.id) from dob_output dobo where dobo.type_script_id=dob.id ) as cell_id ," +
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
            " where dob_script_hash = #{dobScriptHash}")
    CollectionsDto findDetailByScriptHash(@Param("dobScriptHash") byte[] dobScriptHash);

    @Select("<script>" +
            "select * from " +
            "(select *,\n" +
            "case when is_spent=0 and ft_lock_script_id is null then 0 \n" +
            "when is_spent=1 and ft_lock_script_id is null then 2 \n" +
            "else 1 end as action \n" +
            "  from ( select * from (select id,type_script_id,lock_script_id, \n" +
            "   consumed_tx_hash  as tx_hash \n" +
            ",is_spent,consumed_timestamp as block_timestamp,data,\n" +
             "   LEAD(lock_script_id) over w--寻找 to 后一行的  lock_script_id  为null为销毁 \n" +
            "   as ft_lock_script_id\n" +
            "  from dob_output  dob where  exists (\n" +
            "select * from dob_code dc left join dob_extend dobe on dc.dob_extend_id = dobe.id  where dc.dob_code_script_id=dob.type_script_id \n" +
            "  and dobe.dob_script_hash= #{dobScriptHash})  WINDOW w AS (PARTITION BY type_script_id  ORDER BY block_timestamp asc)) where is_spent=1 " +
            "union all \n" +
            "select id,type_script_id,lock_script_id,tx_hash,0 as is_spent,block_timestamp,data,\n" +
            "null as ft_lock_script_id from (\n" +
            "select dob.*,ROW_NUMBER() OVER w  AS rn from dob_output dob where  exists (\n" +
            "select * from dob_code dc left join dob_extend dobe on dc.dob_extend_id = dobe.id \n" +
            "where dc.dob_code_script_id=dob.type_script_id and dobe.dob_script_hash= #{dobScriptHash}) \n" +
            " WINDOW w AS (PARTITION BY type_script_id ORDER BY block_timestamp asc) ) WHERE rn = 1 )  ) where 1=1 \n" +
            " <if test='null != txHash '>  \n" +
            "  and txHash = #{txHash}\n" +
            " </if> \n" +
            " <if test='null != lockScriptId '>  \n" +
            "  and (lock_script_id = #{lockScriptId} or ft_lock_script_id = #{lockScriptId}) \n" +
            " </if> \n" +
            " <if test='null != typeScriptId '>  \n" +
            "  and type_script_id = #{typeScriptId}  \n" +
            " </if> \n" +
            " <if test='null != action '>  \n" +
            "  and action = #{action}  \n" +
            " </if> \n" +
            "  order by block_timestamp  desc  " +
            "</script>")
    Page<NftTransfersDto> transfersPage(Page page ,@Param("dobScriptHash") byte[] dobScriptHash,@Param("txHash") byte[] txHash,@Param("lockScriptId") Long lockScriptId,@Param("typeScriptId") Long typeScriptId,@Param("action") Integer action);

    @Select("<script> " +
            "select dlc.lock_script_id,count(*) as holders_count from dob_live_cells dlc where exists (\n" +
            "select * from dob_code dc left join dob_extend dobe on dc.dob_extend_id = dobe.id \n" +
            "where dc.dob_code_script_id=dlc.type_script_id and dobe.dob_script_hash=#{dobScriptHash})\n" +
            " <if test='null != lockScriptId '>  \n" +
            "  and dlc.lock_script_id = #{lockScriptId} \n" +
            " </if> \n" +
            "group by dlc.lock_script_id \n" +
            " order by ${orderByStr} ${ascOrDesc} " +
            "</script>")
    Page<NftHolderDto> holdersPage(Page page ,@Param("dobScriptHash") byte[] dobScriptHash,@Param("lockScriptId") Long lockScriptId, @Param("orderByStr") String orderByStr,
                                   @Param("ascOrDesc") String ascOrDesc);

    @Select("select dlc.id,dlc.data,dlc.type_script_id,dlc.lock_script_id from dob_live_cells  dlc\n" +
            " where exists (\n" +
            "select * from dob_code dc left join dob_extend dobe on dc.dob_extend_id = dobe.id where \n" +
            "dc.dob_code_script_id=dlc.type_script_id and dobe.dob_script_hash=#{dobScriptHash})")
    Page<NftItemDto> itemsPage(Page page ,@Param("dobScriptHash") byte[] dobScriptHash);

    @Select("select dobo.id,dobo.data,dobo.type_script_id,LAST_VALUE(dobo.lock_script_id) over w as lock_script_id \n" +
            ",de.id as collection_id,de.name as collection_name,FIRST_VALUE(dobo.lock_script_id) over w as create_lock_script_id from \n" +
            " dob_output  dobo left join  dob_code dc \n" +
            " on dc.dob_code_script_id=dobo.type_script_id \n" +
            " left join dob_extend de on de.id= dc.dob_extend_id \n" +
            "where dc.dob_code_script_args=#{args} and de.dob_script_hash=#{dobScriptHash} \n" +
            " WINDOW w AS (PARTITION BY dobo.type_script_id ) limit 1 "
    )
    NftItemDto  itemInfo(@Param("dobScriptHash") byte[] dobScriptHash,@Param("args") byte[] args);

    @Select("select '0x' || encode(de.args, 'hex')                 as collection_id,\n" +
            "       de.name                                        as collection_name,\n" +
            "       '0x' || encode(de.dob_script_hash, 'hex')      as type_script_hash,\n" +
            "       '0x' || encode(dc.dob_code_script_args, 'hex') as token_id\n" +
            "from dob_extend de\n" +
            "         left join dob_code dc on de.id = dc.dob_extend_id\n" +
            "where dc.dob_code_script_args =#{args} \n" +
            "limit 1")
    NftResponse getNftByTokenId(@Param("args") byte[] args);

    @Select("select id                                     as id,\n" +
            "       '0x' || encode(args, 'hex')            as collection_id,\n" +
            "       name                                   as collection_name,\n" +
            "       '0x' || encode(dob_script_hash, 'hex') as type_script_hash\n" +
            "from dob_extend\n" +
            "where LOWER(name) LIKE #{name}")
    List<NftCollectionResponse> getNftCollectionsByName(@Param("name") String name);

    @Select("select id                                     as id,\n" +
            "       '0x' || encode(args, 'hex')            as collection_id,\n" +
            "       name                                   as collection_name,\n" +
            "       '0x' || encode(dob_script_hash, 'hex') as type_script_hash\n" +
            "from dob_extend\n" +
            "where args = #{args}")
    List<NftCollectionResponse> getNftCollectionsByClusterId(@Param("args") byte[] args);

    @Select("select id                                     as id,\n" +
            "       '0x' || encode(args, 'hex')            as collection_id,\n" +
            "       name                                   as collection_name,\n" +
            "       '0x' || encode(dob_script_hash, 'hex') as type_script_hash\n" +
            "from dob_extend\n" +
            "where dob_script_hash = #{clusterTypeHash} limit 1")
  NftCollectionResponse getNftCollectionsByClusterTypeHash(@Param("clusterTypeHash") byte[] clusterTypeHash);

    @Select("select dc.dob_code_script_args,dlc.data,de.name as collection_name,de.dob_script_hash,dlc.id as cell_id \n" +
            " from  dob_live_cells dlc left join dob_code dc  \n " +
            " on dlc.type_script_id = dc.dob_code_script_id \n" +
            " left join  dob_extend de on de.id= dc.dob_extend_id \n" +
            " where dlc.lock_script_id = #{lockScriptId} ")
    List<AccountNftDto> accountNftInfo(@Param("lockScriptId") Long lockScriptId);

}




