package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.dto.CellOutputDto;
import com.ckb.explorer.domain.dto.PendingCellInputDto;
import com.ckb.explorer.entity.Output;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OutputMapper extends BaseMapper<Output> {

  List<CellOutputDto> getCellbaseDisplayOutputs( @Param("transactionId") Long transactionId);

  Page<CellOutputDto> getCellbaseDisplayOutputs(Page page, @Param("transactionId") Long transactionId);

  List<CellOutputDto> getNormalTxDisplayOutputs(@Param("transactionId") Long transactionId);

  Page<CellOutputDto> getNormalTxDisplayOutputs(Page page, @Param("transactionId") Long transactionId);

  List<CellOutputDto> getCellbaseDisplayOutputsByTransactionIds( @Param("transactionIds") List<Long> transactionIds);

  List<CellOutputDto> getNormalTxDisplayOutputsByTransactionIds(@Param("transactionIds") List<Long> transactionIds, @Param("size")int size);

  Long countAddressTransactions(@Param("lockScriptId") Long lockScriptId);

  List<CellOutputDto> getDaoDisplayOutputsByTransactionIds(@Param("transactionIds") List<Long> transactionIds, @Param("size")int size, @Param("typeScriptId") Long typeScriptId);

  @Select("select * from output where lock_script_id = #{lockScriptId} limit 1")
  Output findByLockScriptIdOutput(@Param("lockScriptId") Long lockScriptId);

  PendingCellInputDto getOutputByTxHashAndIndex(@Param("txHash") byte[] txHash, @Param("index") Integer index);

  @Select("<script>" +
          "WITH ranked_transactions AS (\n"
          + "    SELECT\n"
          + "        tx_id,\n"
          + "        block_timestamp,\n"
          + "        -- 对同一交易ID，按要求排序\n"
          + "        ROW_NUMBER() OVER (\n"
          + "            PARTITION BY tx_id\n"
          + "            ORDER BY ${orderByStr} ${ascOrDesc}\n"
          + "        ) AS rn\n"
          + "    FROM output o \n"
          + "    WHERE type_script_id = #{typeScriptId}\n"
          + "    <if test='null != txHash'>\n"
          + "       and tx_hash = #{txHash}\n"
          + "     </if> "
          + "     <if test='null != lockScriptId'>\n"
          + "       and (lock_script_id = #{lockScriptId} or exists (select 1 from output o1 where o1.consumed_tx_hash =o.tx_hash and o.lock_script_id = #{lockScriptId} ))\n "
          + "      </if> "
          + ")\n"
          + "SELECT tx_id\n"
          + "FROM ranked_transactions\n"
          + "WHERE rn = 1  -- 筛选出每个交易ID的最新一条记录\n"
          + "ORDER BY ${orderByStr} ${ascOrDesc}\n"
          + "</script>")
  @Deprecated
  Page<Long> getUdtTransactionHashes(Page page, @Param("typeScriptId") Long typeScriptId , @Param("orderByStr") String orderByStr,
                                       @Param("ascOrDesc") String ascOrDesc, @Param("txHash") byte[] txHash, @Param("lockScriptId") Long lockScriptId);

   static final String findByTypeScriptIdString = "WITH ranked_output AS (\n"
          + "    SELECT\n"
          + "        tx_hash,\n"
          + "        block_timestamp,\n"
          + "        consumed_tx_hash,\n"
          + "        consumed_timestamp, \n"
          + "        is_spent\n"
          + "    FROM output o \n"
          + "    WHERE type_script_id = #{typeScriptId}\n"
          + "    <if test='null != txHash'>\n"
          + "       and tx_hash = #{txHash}   \n"
          + "     </if> "
          + "     <if test='null != lockScriptId'>\n"
          + "       and (lock_script_id = #{lockScriptId} or exists (select 1 from output o1 where o1.consumed_tx_hash =o.tx_hash and o.lock_script_id = #{lockScriptId} ))\n "
          + "      </if> "
          + ")\n"
          + "SELECT  tx_hash from(\n"
          + "SELECT distinct r1.tx_hash,r1.block_timestamp from ranked_output r1  \n"
          + "union all  \n"
          + "select distinct r2.consumed_tx_hash AS tx_hash, r2.consumed_timestamp AS block_timestamp \n"
          + "from  ranked_output  r2 where not exists (select 1 from ranked_output r where r.tx_hash=r2.consumed_tx_hash ) and r2.is_spent=1)  where 1=1 \n"
          + "<if test='null != txHash'>\n"
          + "       and tx_hash = #{txHash} \n"
          + "     </if> ";
  @Select("<script>" +
          findByTypeScriptIdString
          + "ORDER BY ${orderByStr} ${ascOrDesc}\n"
          + "</script>")
  Page<byte[]> getUdtTransactionTxHashes(Page page, @Param("typeScriptId") Long typeScriptId , @Param("orderByStr") String orderByStr,
                                     @Param("ascOrDesc") String ascOrDesc, @Param("txHash") byte[] txHash, @Param("lockScriptId") Long lockScriptId);

  @Select("<script>" +
          "select count(*) from (\n"+
          findByTypeScriptIdString+
          ")\n"+
         "</script>")
  Long getUdtTransactionTxHashesTotal(@Param("typeScriptId") Long typeScriptId ,  @Param("txHash") byte[] txHash, @Param("lockScriptId") Long lockScriptId);
}