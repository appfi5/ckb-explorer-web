package com.ckb.explorer.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.dto.UdtH24TransactionsCountDto;
import com.ckb.explorer.entity.Address24hTransaction;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author dell
* @description 针对表【address_24h_transaction】的数据库操作Mapper
* @createDate 2025-09-03 17:37:17
* @Entity com.ckb.explore.worker.entity.Address24hTransaction
*/
@DS("risingwave")
public interface Address24hTransactionMapper extends BaseMapper<Address24hTransaction> {

  @Select("WITH ranked_transactions AS (\n"
      + "    SELECT\n"
      + "        t.ckb_transaction_id,\n"
      + "        t.block_timestamp,\n"
      + "        ROW_NUMBER() OVER (\n"
      + "            PARTITION BY t.ckb_transaction_id\n"
      + "            ORDER BY t.${orderByStr} ${ascOrDesc}\n"
      + "        ) AS rn\n"
      + "    FROM address_24h_transaction t\n"
      + "    WHERE t.lock_script_id = #{lockScriptId}\n"
      + ")\n"
      + "SELECT rt.ckb_transaction_id\n"
      + "FROM ranked_transactions rt\n"
      + "WHERE rt.rn = 1  \n"
      + "ORDER BY rt.${orderByStr} ${ascOrDesc}")
  Page<Long> getTransactionsLast24hrsByLockScriptIdWithSort(Page page, @Param("lockScriptId") Long lockScriptId , @Param("orderByStr") String orderByStr, @Param("ascOrDesc") String ascOrDesc);


    List<UdtH24TransactionsCountDto> getTransactionsCountByScriptIds(@Param("scriptIds") List<Long> scriptIds);


  @Select("<script>" +
            "WITH ranked_transactions AS (\n"
            + "    SELECT\n"
            + "        ckb_transaction_id,\n"
            + "        block_timestamp,\n"
            + "        -- 对同一交易ID，按要求排序\n"
            + "        ROW_NUMBER() OVER (\n"
            + "            PARTITION BY ckb_transaction_id\n"
            + "            ORDER BY ${orderByStr} ${ascOrDesc}\n"
            + "        ) AS rn\n"
            + "    FROM address_24h_transaction ad \n"
            + "    WHERE type_script_id = #{typeScriptId}\n"
            + "    <if test='null != txId'>\n"
            + "       and ckb_transaction_id = #{txId}\n"
            + "     </if> "
            + "     <if test='null != lockScriptId'>\n"
            + "       and lock_script_id = #{lockScriptId}\n "
            + "      </if> "
            + ")\n"
            + "SELECT ckb_transaction_id\n"
            + "FROM ranked_transactions\n"
            + "WHERE rn = 1  -- 筛选出每个交易ID的最新一条记录\n"
            + "ORDER BY ${orderByStr} ${ascOrDesc}\n"
            + "</script>")
    Page<Long> getTransactionsLast24hrsByTypeScriptIdWithSort(Page page, @Param("typeScriptId") Long typeScriptId , @Param("orderByStr") String orderByStr,
                                                              @Param("ascOrDesc") String ascOrDesc,@Param("txId") Long txId, @Param("lockScriptId") Long lockScriptId);


}




