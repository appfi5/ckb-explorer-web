package com.ckb.explorer.utils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.HexFormat;

public class CkbArrayHashType {
  private static final HexFormat HEX_FORMAT = HexFormat.of().withUpperCase();
  private final String hashPrefix;
  private final int hashLength;

  public CkbArrayHashType(String hashPrefix, int hashLength) {
    this.hashPrefix = hashPrefix;
    this.hashLength = hashLength;
  }

  /**
   * 将字节数组转换为带前缀的哈希字符串数组
   */
  public List<String> deserialize(byte[] bytes) {
    if (bytes == null || bytes.length < 2) {
      return null;
    }
    ByteBuffer buffer = ByteBuffer.wrap(bytes);
    int arraySize = buffer.getShort() & 0xFFFF; // 读取 2 字节无符号短整型（大端序）

    List<String> result = new ArrayList<>();
    for (int i = 0; i < arraySize; i++) {
      byte[] hashBytes = new byte[hashLength];
      buffer.get(hashBytes);
      String hex = HEX_FORMAT.formatHex(hashBytes);
      result.add(hashPrefix + hex.toLowerCase());
    }
    return result;
  }

  /**
   * 将带前缀的哈希字符串数组转换为字节数组
   */
  public byte[] serialize(List<String> hashes) {
    if (hashes == null || hashes.isEmpty()) {
      return null;
    }

    int size = hashes.size();
    int totalLength = 2 + size * hashLength; // 2 字节长度 + 每个哈希值
    ByteBuffer buffer = ByteBuffer.allocate(totalLength);
    buffer.putShort((short) size); // 写入数组长度（大端序）

    for (String hash : hashes) {
      String hex = hash.startsWith(hashPrefix)?hash.substring(hashPrefix.length()):hash;
      byte[] bytes = HEX_FORMAT.parseHex(hex);
      buffer.put(bytes);
    }

    return buffer.array();
  }
}