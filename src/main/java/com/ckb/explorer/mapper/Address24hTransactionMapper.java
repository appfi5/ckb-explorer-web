package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.entity.Address24hTransaction;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
* @author dell
* @description 针对表【address_24h_transaction】的数据库操作Mapper
* @createDate 2025-09-03 17:37:17
* @Entity com.ckb.explore.worker.entity.Address24hTransaction
*/
public interface Address24hTransactionMapper extends BaseMapper<Address24hTransaction> {

    @Select("WITH ranked_transactions AS (\n"
        + "    SELECT\n"
        + "        ckb_transaction_id,\n"
        + "        block_timestamp,\n"
        + "        -- 对同一交易ID，按要求排序\n"
        + "        ROW_NUMBER() OVER (\n"
        + "            PARTITION BY ckb_transaction_id\n"
        + "            ORDER BY ${orderByStr} ${ascOrDesc}\n"
        + "        ) AS rn\n"
        + "    FROM address_24h_transaction\n"
        + "    WHERE lock_script_id = #{lockScriptId}\n"
        + ")\n"
        + "-- 只保留每个交易ID的最新记录，并按要求排序\n"
        + "SELECT ckb_transaction_id\n"
        + "FROM ranked_transactions\n"
        + "WHERE rn = 1  -- 筛选出每个交易ID的最新一条记录\n"
        + "ORDER BY ${orderByStr} ${ascOrDesc}")
    Page<Long> getTransactionsLast24hrsByLockScriptIdWithSort(Page page, @Param("lockScriptId") Long lockScriptId , @Param("orderByStr") String orderByStr, @Param("ascOrDesc") String ascOrDesc);
}




