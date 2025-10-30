package com.ckb.explorer.service.impl;

import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.resp.AddressResponse;
import com.ckb.explorer.domain.resp.BlockResponse;
import com.ckb.explorer.domain.resp.LockScriptResponse;
import com.ckb.explorer.domain.resp.NftResponse;
import com.ckb.explorer.domain.resp.NftCollectionResponse;
import com.ckb.explorer.domain.resp.TransactionResponse;
import com.ckb.explorer.domain.resp.TypeScriptResponse;
import com.ckb.explorer.facade.IBlockCacheFacade;
import com.ckb.explorer.facade.ICkbTransactionCacheFacade;
import com.ckb.explorer.facade.IScriptCacheFacade;
import com.ckb.explorer.service.DobExtendService;
import com.ckb.explorer.service.ScriptService;
import com.ckb.explorer.service.SuggestQueryService;
import com.ckb.explorer.util.I18n;
import com.ckb.explorer.util.QueryKeyUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SuggestQueryServiceImpl implements SuggestQueryService {

  @Resource
  private I18n i18n;

  @Resource
  private QueryKeyUtils queryKeyUtils;

  @Resource
  private IBlockCacheFacade blockCacheFacade;

  @Resource
  private ICkbTransactionCacheFacade ckbTransactionCacheFacade;

  @Resource
  private IScriptCacheFacade scriptCacheFacade;

  @Resource
  private ScriptService scriptService;

  @Value("${ckb.typeIdCodeHash: 0x00000000000000000000000000000000000000000000000000545950455f4944}")
  private String typeIdCodeHash;

   @Resource
   private DobExtendService dobExtendService;

  // @Resource
  // private BitcoinTransactionService bitcoinTransactionService;

  /**
   * 查询
   *
   * @param queryKey 查询关键字
   * @param filterBy 过滤条件
   *                 0-未指定；1-块哈希；2-地址LockHash；3-TypeHash；4-typeId的args；5-lock的codeHash；6-type的codeHash;
   *                 7-比特币交易的txid；8-比特币地址哈希；9-Udt的name_or_symbol；10-nft_collections的sn；11-nft_collections的name；12-fiber_graph_nodes
   * @return
   */
  @Override
  public Object find(String queryKey, Integer filterBy) {
    // 初始化查询参数
    queryKey = processQueryKey(queryKey);

    // filterBy未指定时聚合查询
    if (filterBy == null || filterBy == 0) {
      return aggregateQuery(queryKey);
      // 否则单个查询
    } else {
      return singleQuery(queryKey, filterBy);
    }
  }

  /**
   * 处理查询关键字
   */
  private String processQueryKey(String queryKey) {
    if (StringUtils.isBlank(queryKey)) {
      return queryKey;
    }
    // 如果包含字母，则转为小写
    if (containsLetter(queryKey)) {
      return queryKey.toLowerCase();
    }
    return queryKey;
  }

  /**
   * 检查字符串是否包含字母
   */
  private boolean containsLetter(String keyword) {
    if (StringUtils.isBlank(keyword)) {
      return false;
    }
    return keyword.matches(".*[a-zA-Z].*");
  }

  /**
   * 单个查询
   *
   * @param queryKey 查询关键字
   * @param filterBy 过滤条件
   *                 0-未指定；1-块哈希；2-地址LockHash；3-TypeHash；4-typeId的args；5-lock的codeHash；6-type的codeHash;
   *                 7-比特币交易的txid；8-比特币地址哈希；9-Udt的name_or_symbol；10-nft的tokenId；11-nft_collections的clusterName；12-nft_collections的clusterId;13-nft_collections的clusterTypeHash
   */
  private Object singleQuery(String queryKey, Integer filterBy) {
    Object result = null;

    switch (filterBy) {
      case 1:// 按块哈希查询
        vaildHex(queryKey);
        result = findCachedBlock(queryKey);
        break;
      case 2:// 按地址LockScriptHash查询
        vaildHex(queryKey);
        result = findAddressByLockHash(queryKey);
        break;
      case 3:// 按TypeScriptHash查询合约 udt前端自己查了，其他不要
        //vaildHex(queryKey);
        //result = findUdtByTypeHash(queryKey);
        break;
      case 4:// 按typeId的args查询UDT
        vaildHex(queryKey);
        result = findTypeScriptByTypeId(queryKey);
        break;
      case 5:// 按lock的codeHash查询合约
        vaildHex(queryKey);
        result = findLockScriptByCodeHash(queryKey);
        break;
      case 6:// 按type的codeHash查询合约
        vaildHex(queryKey);
        result = findTypeScriptByCodeHash(queryKey);
        break;
      case 7:// 比特币交易的txid
        vaildHex(queryKey);
        result = findBitcoinTransactionByTxid(queryKey);
        break;
      case 8:// 比特币地址哈希
        vaildHex(queryKey);
        result = findBitcoinAddress(queryKey);
        break;
      case 9:// 按udt的name或symbol查询udt 前端自己查了
        //result = findUdtsByNameOrSymbol(queryKey);
        break;
      case 10:// 10-nft的tokenId
        vaildHex(queryKey);
        result = findNftByTokenId(queryKey);
        break;
      case 11:// 按nft的name查询nft
        result = findNftCollectionsByName(queryKey);
        break;
      case 12:// nft_collections的clusterId
        vaildHex(queryKey);
        result = findNftCollectionsByClusterId(queryKey);
        break;
      case 13:// nft_collections的clusterTypeHash
        vaildHex(queryKey);
        result = findNftCollectionsByClusterTypeHash(queryKey);
        break;
    }

    if (result == null) {
      throw new ServerException(I18nKey.SUGGEST_QUERY_RESULT_NOT_FOUND_CODE,
          i18n.getMessage(I18nKey.SUGGEST_QUERY_RESULT_NOT_FOUND_MESSAGE));
    }

    return result;
  }

  private void vaildHex(String queryKey){
    if (!queryKeyUtils.isValidHex(queryKey))
      throw new ServerException(I18nKey.SUGGEST_QUERY_KEY_INVALID_CODE,
          i18n.getMessage(I18nKey.SUGGEST_QUERY_KEY_INVALID_MESSAGE));
  }

  /**
   * 聚合查询 只支持按块高查块，按交易哈希查交易，按地址字符串查地址
   */
  private Object aggregateQuery(String queryKey) {

    Object result = null;
    // 如果是纯数字，查询区块
    if (queryKeyUtils.isIntegerString(queryKey)) {
      result = findCachedBlock(queryKey);
    }

    // 如果字符串长度小于2，查询结果为空
    if (queryKey.length() < 2) {
      throw new ServerException(I18nKey.SUGGEST_QUERY_RESULT_NOT_FOUND_CODE,
          i18n.getMessage(I18nKey.SUGGEST_QUERY_RESULT_NOT_FOUND_MESSAGE));
    }

    if (queryKeyUtils.isValidHex(queryKey)) {
      result = findCkbTransactionByHash(queryKey);
    }

    if (queryKeyUtils.isValidAddress(queryKey)) {
      // 查询地址
      result = findCachedAddress(queryKey);

    }

    if (result == null) {
      throw new ServerException(I18nKey.SUGGEST_QUERY_RESULT_NOT_FOUND_CODE,
          i18n.getMessage(I18nKey.SUGGEST_QUERY_RESULT_NOT_FOUND_MESSAGE));
    }

    return result;
  }

  /**
   * 查询缓存的区块(根据区块哈希或区块号)
   */
  private BlockResponse findCachedBlock(String queryKey) {
    return blockCacheFacade.findBlock(queryKey);
  }

  /**
   * 根据哈希查询CKB交易
   */
  private TransactionResponse findCkbTransactionByHash(String queryKey) {
    return ckbTransactionCacheFacade.getTransactionByHash(queryKey);
  }

  /**
   * 根据Script哈希查询地址
   */
  private AddressResponse findAddressByLockHash(String queryKey) {

    return scriptCacheFacade.getAddressInfo(queryKey);
  }

  /**
   * 根据地址查询
   */
  private AddressResponse findCachedAddress(String queryKey) {

    return scriptCacheFacade.getAddressInfo(queryKey);
  }

  /**
   * 根据类型哈希查询UDT
   */
  private Object findUdtByTypeHash(String queryKey) {
    // 根据TypeHash查Script+typeScriptExtend+dob_ext 不需要查了，当前不用
    // 如果是udt 返回UdtSerializer
    var script = scriptService.findByScriptHash(queryKey);
    if (script == null || script.getIsTypescript() ==  0) {
      return null;
    }
    // 判断是否是udt

    return null;
  }

  /**
   * 根据args查询类型脚本
   */
  private TypeScriptResponse findTypeScriptByTypeId(String queryKey) {

    return scriptService.findTypeScriptByTypeId(queryKey,typeIdCodeHash);

  }

  /**
   * 根据代码哈希查询类型脚本
   */
  private TypeScriptResponse findTypeScriptByCodeHash(String queryKey) {

    return scriptService.findTypeScriptByCodeHash(queryKey);
  }

  /**
   * 根据代码哈希查询Lock脚本
   */
  private LockScriptResponse findLockScriptByCodeHash(String queryKey) {
    return scriptService.findLockScriptByCodeHash(queryKey);
  }

  /**
   * 根据TXID查询比特币交易 二期不做比特币
   */
  private Object findBitcoinTransactionByTxid(String queryKey) {
    // 注意：此处为示例实现，实际项目中需要根据具体情况调整
    // try {
    //     String txid = queryKey.replaceFirst("^0x", ""); // 移除前缀
    //     return bitcoinTransactionService.findByTxid(txid);
    // } catch (Exception e) {
    //     return null;
    // }
    return null; // 占位实现
  }

  /**
   * 查询比特币地址 二期不做比特币
   */
  private Object findBitcoinAddress(String queryKey) {
    // 注意：此处为示例实现，实际项目中需要根据具体情况调整
    return null; // 占位实现
  }

  /**
   * 根据名称或符号查询UDT 前端自查不用实现
   */
  private List<Object> findUdtsByNameOrSymbol(String queryKey) {
    // 注意：此处为示例实现，实际项目中需要根据具体情况调整
    // try {
    //     return udtService.findByNameOrSymbol(queryKey);
    // } catch (Exception e) {
    //     return null;
    // }
    return null; // 占位实现
  }

  /**
   * 根据TokenId查询NFT信息
   */
  private NftResponse findNftByTokenId(String queryKey) {

    return dobExtendService.getNftByTokenId(queryKey);
  }

  /**
   * 根据名称查询NFT集合
   */
  private List<NftCollectionResponse> findNftCollectionsByName(String queryKey) {
    return dobExtendService.getNftCollectionsByName(queryKey);
  }

  private List<NftCollectionResponse> findNftCollectionsByClusterId(String queryKey) {
    return dobExtendService.getNftCollectionsByClusterId(queryKey);
  }

  private NftCollectionResponse findNftCollectionsByClusterTypeHash(String queryKey) {
    return dobExtendService.getNftCollectionsByClusterTypeHash(queryKey);
  }
}