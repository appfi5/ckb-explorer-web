package com.ckb.explorer.utils;

import java.util.HexFormat;

public class CkbHashType {
  private static final HexFormat HEX_FORMAT = HexFormat.of().withUpperCase();
  private final String hashPrefix;

  public CkbHashType(String hashPrefix) {
    this.hashPrefix = hashPrefix;
  }

  /**
   * 将字节数组转换为带前缀的十六进制字符串（如 "0x..."）
   */
  public String deserialize(byte[] bytes) {
    if (bytes == null || bytes.length == 0) {
      return null;
    }
    String hex = HEX_FORMAT.formatHex(bytes);
    return hashPrefix + hex.toLowerCase();
  }

  /**
   * 将带前缀的十六进制字符串转换为字节数组
   */
  public byte[] serialize(String hexString) {
    if (hexString == null || hexString.isEmpty()) {
      return null;
    }

    String hex = hexString.startsWith(hashPrefix)?hexString.substring(hashPrefix.length()):hexString;
    return HEX_FORMAT.parseHex(hex);
  }
}