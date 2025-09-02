package com.ckb.explorer.service.impl;

import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.resp.AddressResponse;
import com.ckb.explorer.domain.resp.BlockResponse;
import com.ckb.explorer.domain.resp.LockScriptResponse;
import com.ckb.explorer.domain.resp.TransactionResponse;
import com.ckb.explorer.domain.resp.TypeScriptResponse;
import com.ckb.explorer.facade.IBlockCacheFacade;
import com.ckb.explorer.facade.ICkbTransactionCacheFacade;
import com.ckb.explorer.facade.IScriptCacheFacade;
import com.ckb.explorer.service.ScriptService;
import com.ckb.explorer.service.SuggestQueryService;
import com.ckb.explorer.util.I18n;
import com.ckb.explorer.util.QueryKeyUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
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

//  @Value("ckb.typeIdCodeHash")
//  private String typeIdCodeHash;

  // 假设的服务，实际项目中可能需要创建或调整
  // @Resource
  // private UdtService udtService;

  // @Resource
  // private TokenCollectionService tokenCollectionService;

  // @Resource
  // private BitcoinTransactionService bitcoinTransactionService;

  @Override
  public Object find(String queryKey, Integer filterBy) {
    // 初始化查询参数
    queryKey = processQueryKey(queryKey);

    if (filterBy != null && filterBy == 0) {
      return aggregateQuery(queryKey);
    } else {
      return singleQuery(queryKey);
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
   */
  private Object singleQuery(String queryKey) {
    Object result = null;

    // 第一版只支持按高度查区块，按哈希查交易，按地址字符串查地址
    if (queryKeyUtils.isIntegerString(queryKey)) {
      // 查询区块
      result = findCachedBlock(queryKey);
    } else if (queryKeyUtils.isValidHex(queryKey)) {
      // 尝试多种查询方法
      List<Supplier<Object>> queryMethods = Arrays.asList(
//          () -> findCachedBlock(queryKey),
          () -> findCkbTransactionByHash(queryKey)
//          () -> findAddressByLockHash(queryKey),
//          () -> findUdtByTypeHash(queryKey),
//          () -> findTypeScriptByTypeId(queryKey),
//          () -> findTypeScriptByCodeHash(queryKey),
//          () -> findLockScriptByCodeHash(queryKey),
//          () -> findBitcoinTransactionByTxid(queryKey),
//          () -> findNftCollectionsBySn(queryKey)
      );

      // 并行执行所有查询方法
      List<CompletableFuture<Object>> futures = queryMethods.stream()
          .map(method -> CompletableFuture.supplyAsync(method))
          .collect(Collectors.toList());

      // 等待所有查询完成并收集结果
      for (CompletableFuture<Object> future : futures) {
        try {
          Object tempResult = future.get();
          if (tempResult != null) {
            result = tempResult;
            break; // 找到第一个结果就返回
          }
        } catch (InterruptedException | ExecutionException e) {
          // 忽略异常，继续下一个查询
          Thread.currentThread().interrupt();
        }
      }
    } else if (queryKeyUtils.isValidAddress(queryKey)) {
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
   * 聚合查询
   */
  private List<Object> aggregateQuery(String queryKey) {
    List<Object> dataList = new ArrayList<>();
    // 第一版只支持按高度查区块，按哈希查交易，按地址字符串查地址
    // 如果是纯数字，查询区块
    if (queryKeyUtils.isIntegerString(queryKey)) {
      BlockResponse block = findCachedBlock(queryKey);
      if (block != null) {
        dataList.add(block);
        return dataList;
      }
    }

    // 如果字符串长度小于2，查询结果为空
    if (queryKey.length() < 2) {
      throw new ServerException(I18nKey.SUGGEST_QUERY_RESULT_NOT_FOUND_CODE,
          i18n.getMessage(I18nKey.SUGGEST_QUERY_RESULT_NOT_FOUND_MESSAGE));
    }

    // 并行执行多种查询
    CompletableFuture<Void> allFutures = CompletableFuture.runAsync(() -> {
      if (queryKeyUtils.isValidHex(queryKey)) {
        // 执行各种hex查询
        List<Supplier<Object>> hexQueryMethods = Arrays.asList(
//            () -> findCachedBlock(queryKey),
            () -> findCkbTransactionByHash(queryKey)
//            () -> findAddressByLockHash(queryKey),
//            () -> findUdtByTypeHash(queryKey),
//            () -> findTypeScriptByTypeId(queryKey),
//            () -> findTypeScriptByCodeHash(queryKey),
//            () -> findLockScriptByCodeHash(queryKey),
//            () -> findBitcoinTransactionByTxid(queryKey),
//            () -> findNftCollectionsBySn(queryKey)
        );

        hexQueryMethods.forEach(method -> {
          try {
            Object result = method.get();
            if (result != null) {
              synchronized (dataList) {
                dataList.add(result);
              }
            }
          } catch (Exception e) {
            // 忽略异常
          }
        });
      }

      if (queryKeyUtils.isValidAddress(queryKey)) {
        // 查询地址
        try {
          Object address = findCachedAddress(queryKey);
          if (address != null) {
            synchronized (dataList) {
              dataList.add(address);
            }
          }
        } catch (Exception e) {
          // 忽略异常
        }
      }

      // 查询比特币地址
//      try {
//        Object bitcoinAddress = findBitcoinAddress(queryKey);
//        if (bitcoinAddress != null) {
//          synchronized (dataList) {
//            dataList.add(bitcoinAddress);
//          }
//        }
//      } catch (Exception e) {
//        // 忽略异常
//      }

      // 查询UDT
//      try {
//        List<Object> udts = findUdtsByNameOrSymbol(queryKey);
//        if (udts != null) {
//          synchronized (dataList) {
//            dataList.addAll(udts);
//          }
//        }
//      } catch (Exception e) {
//        // 忽略异常
//      }

      // 查询NFT集合
//      try {
//        List<Object> collections = findNftCollectionsByName(queryKey);
//        if (collections != null) {
//          synchronized (dataList) {
//            dataList.addAll(collections);
//          }
//        }
//      } catch (Exception e) {
//        // 忽略异常
//      }

      // 查询Fiber图节点
//      try {
//        List<Object> nodes = findFiberGraphNodes(queryKey);
//        if (nodes != null) {
//          synchronized (dataList) {
//            dataList.addAll(nodes);
//          }
//        }
//      } catch (Exception e) {
//        // 忽略异常
//      }
    });

    // 等待所有查询完成
    allFutures.join();

    if (dataList.isEmpty()) {
      throw new ServerException(I18nKey.SUGGEST_QUERY_RESULT_NOT_FOUND_CODE,
          i18n.getMessage(I18nKey.SUGGEST_QUERY_RESULT_NOT_FOUND_MESSAGE));
    }

    return dataList;
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
   * 根据类型哈希查询UDT TODO
   */
  private Object findUdtByTypeHash(String queryKey) {
    // 注意：此处为示例实现，实际项目中需要根据具体情况调整
    // try {
    //     return udtCacheFacade.findByTypeHash(queryKey);
    // } catch (Exception e) {
    //     return null;
    // }
    return null; // 占位实现
  }

  /**
   * 根据args查询类型脚本 第一版不支持
   */
  private TypeScriptResponse findTypeScriptByTypeId(String queryKey) {

    return null;

  }

  /**
   * 根据代码哈希查询类型脚本 第一版不支持
   */
  private TypeScriptResponse findTypeScriptByCodeHash(String queryKey) {

    return scriptService.findTypeScriptByCodeHash(queryKey);
  }

  /**
   * 根据代码哈希查询Lock脚本 第一版不支持
   */
  private LockScriptResponse findLockScriptByCodeHash(String queryKey) {
    // 注意：此处为示例实现，实际项目中需要根据具体情况调整
    return scriptService.findLockScriptByCodeHash(queryKey);
  }

  /**
   * 根据TXID查询比特币交易 TODO 第一版不支持
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
   * 查询比特币地址 TODO 第一版不支持
   */
  private Object findBitcoinAddress(String queryKey) {
    // 注意：此处为示例实现，实际项目中需要根据具体情况调整
    return null; // 占位实现
  }

  /**
   * 根据名称或符号查询UDT TODO
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
   * 根据SN查询NFT集合 TODO
   */
  private List<Object> findNftCollectionsBySn(String queryKey) {
    // 注意：此处为示例实现，实际项目中需要根据具体情况调整
    // try {
    //     return tokenCollectionService.findBySn(queryKey);
    // } catch (Exception e) {
    //     return null;
    // }
    return null; // 占位实现
  }

  /**
   * 根据名称查询NFT集合 TODO
   */
  private List<Object> findNftCollectionsByName(String queryKey) {
    // 注意：此处为示例实现，实际项目中需要根据具体情况调整
    // try {
    //     return tokenCollectionService.findByName(queryKey);
    // } catch (Exception e) {
    //     return null;
    // }
    return null; // 占位实现
  }

  /**
   * 查询Fiber图节点 TODO
   */
  private List<Object> findFiberGraphNodes(String queryKey) {
    // 注意：此处为示例实现，实际项目中需要根据具体情况调整
    return null; // 占位实现
  }
}