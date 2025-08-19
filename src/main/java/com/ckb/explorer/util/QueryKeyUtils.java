package com.ckb.explorer.util;

import org.nervos.ckb.utils.address.Address;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class QueryKeyUtils {

  // 从配置中注入 default_hash_prefix 和 default_with_prefix_hash_length
  @Value("${ckb.default-hash-prefix}")
  private String defaultHashPrefix;

  @Value("${ckb.default-with-prefix-hash-length}")
  private int defaultWithPrefixHashLength;

  /**
   * 检查字符串是否为纯整数（正整数）
   */
  public boolean isIntegerString(String queryKey) {
    if (queryKey == null || queryKey.isEmpty()) {
      return false;
    }
    return queryKey.matches("\\d+");
  }

  /**
   * 检查是否为有效的十六进制字符串（带前缀）
   */
  public boolean isValidHex(String queryKey) {
    return startWithDefaultHashPrefix(queryKey) &&
        lengthIsValid(queryKey) &&
        isHexString(queryKey);
  }

  /**
   * 检查是否以默认哈希前缀开头（如 "0x"）
   */
  public boolean startWithDefaultHashPrefix(String queryKey) {
    if (queryKey == null || defaultHashPrefix == null) {
      return false;
    }
    return queryKey.startsWith(defaultHashPrefix);
  }

  /**
   * 检查长度是否有效
   */
  public boolean lengthIsValid(String queryKey) {
    if (queryKey == null) {
      return false;
    }
    return queryKey.length() == defaultWithPrefixHashLength;
  }

  /**
   * 检查去掉前缀后的部分是否为合法的十六进制字符
   */
  public boolean isHexString(String queryKey) {
    if (queryKey == null || defaultHashPrefix == null) {
      return false;
    }
    if (!queryKey.startsWith(defaultHashPrefix)) {
      return false;
    }
    String hexPart = queryKey.substring(defaultHashPrefix.length());
    return hexPart.matches("[0-9a-fA-F]*"); // 允许空字符串
  }

  /**
   * 检查是否为有效的地址（调用 CkbUtils.parseAddress）
   */
  public boolean isValidAddress(String queryKey) {
    try {
      Address.decode(queryKey);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
