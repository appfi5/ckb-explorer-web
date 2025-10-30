package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ckb.explorer.domain.dto.TransactionDto;
import com.ckb.explorer.domain.resp.AddressTransactionPageResponse;
import com.ckb.explorer.domain.resp.ContractTransactionPageResponse;
import com.ckb.explorer.domain.resp.TransactionPageResponse;
import com.ckb.explorer.entity.CkbTransaction;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CkbTransactionMapper extends BaseMapper<CkbTransaction> {

  TransactionDto selectTransactionWithCellDeps(@Param("txHash") byte[] txHash);

  Page<AddressTransactionPageResponse> selectPageByAddressScriptId(Page page, @Param("orderByStr") String orderBy, @Param("ascOrDesc") String ascOrDesc, @Param("addressScriptId") Long addressScriptId);

  Page<CkbTransaction> selectPageByBlockHash(Page page, @Param("blockHash") byte[] blockHash, @Param("txHash") byte[] txHash, @Param("lockScriptId") Long lockScriptId);

  List<AddressTransactionPageResponse> selectByTransactionIds( @Param("transactionIds") List<Long> transactionIds, @Param("orderByStr") String orderBy, @Param("ascOrDesc") String ascOrDesc);

  @Select("SELECT id, '0x' || encode(tx_hash, 'hex') as transaction_hash, block_number,block_timestamp,capacity_involved,output_count - input_count as live_cell_changes FROM ckb_transaction WHERE (tx_index <> 0) ORDER BY id DESC LIMIT #{size}")
  List<TransactionPageResponse> getHomePageTransactions(@Param("size") int size);

  @Select("SELECT id,'0x' || encode(tx_hash, 'hex') as transaction_hash, block_number,block_timestamp,capacity_involved,output_count - input_count as live_cell_changes FROM ckb_transaction WHERE (tx_index <> 0) ORDER BY id DESC")
  Page<TransactionPageResponse> getPageTransactions(Page page);

  /**
   * 查询合约相关交易
   * @param txHashs 交易哈希列表
   * @return 合约交易列表
   */
  List<ContractTransactionPageResponse> selectContractTransactions(@Param("txHashs") List<byte[]> txHashs);

  Page<ContractTransactionPageResponse> getPageContractTransactions(Page page, @Param("lockScriptId") Long lockScriptId, @Param("typeScriptId") Long typeScriptId);
}