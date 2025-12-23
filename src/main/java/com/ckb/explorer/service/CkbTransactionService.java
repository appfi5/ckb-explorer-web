package com.ckb.explorer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ckb.explorer.domain.req.ContractTransactionsPageReq;
import com.ckb.explorer.domain.req.UdtTransactionsPageReq;
import com.ckb.explorer.domain.resp.*;
import com.ckb.explorer.entity.CkbTransaction;

import java.time.LocalDate;
import java.util.List;

public interface CkbTransactionService extends IService<CkbTransaction> {

  /**
   * 获取交易列表
   * @param size
   * @return
   */
  List<TransactionPageResponse> getHomePageTransactions(int size);

  /**
   * 分页查询交易列表
   * @param pageNum 当前页码
   * @param pageSize 每页条数
   * @param sort 排序字段和方式，格式为"字段名.排序方式"，如"number.desc"
   * @return 分页结果
   */
  Page<TransactionPageResponse> getCkbTransactionsByPage(int pageNum, int pageSize, String sort);

  /**
   * 根据交易哈希获取交易详情
   *
   * @param txHash 交易哈希
   * @return 交易详情响应
   */
  TransactionResponse getTransactionByHash(String txHash);
  
  /**
   * 获取交易的显示输入
   * 
   * @param txHash 交易哈希
   * @param page 页码
   * @param pageSize 每页大小
   * @return 分页的输入单元格列表
   */
  Page<CellInputResponse> getDisplayInputs(String txHash, int page, int pageSize);

  /**
   * 获取交易的显示输出
   * 
   * @param txHash 交易哈希
   * @param page 页码
   * @param pageSize 每页大小
   * @return 分页的输出单元格列表
   */
  Page<CellOutputResponse> getDisplayOutputs(String txHash, int page, int pageSize);

  /**
   * 获取块里的交易
   * @param blockHash
   * @param txHash
   * @param addressHash
   * @param page
   * @param pageSize
   * @return
   */
  Page<BlockTransactionPageResponse> getBlockTransactions(String blockHash, String txHash, String addressHash, int page, int pageSize);

  /**
   * 获取地址里的交易
   * @param address
   * @param sort
   * @param page
   * @param pageSize
   * @return
   */
  Page<AddressTransactionPageResponse> getAddressTransactions(String address, String sort, int page, int pageSize, LocalDate startTime, LocalDate endTime);

  /**
   * UDT相关交易
   * @param typeScriptHash
   * @param req udt请求参数交易
   * @return
   */
  Page<UdtTransactionPageResponse> getUdtTransactions(String typeScriptHash, UdtTransactionsPageReq req);

  /**
   * 获取合约相关交易
   * @param req 合约交易请求参数
   * @return
   */
  Page<ContractTransactionPageResponse> getContractTransactions(ContractTransactionsPageReq req);
}