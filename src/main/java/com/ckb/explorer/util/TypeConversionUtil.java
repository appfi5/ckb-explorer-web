package com.ckb.explorer.util;

import java.util.HexFormat;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class TypeConversionUtil {

  /**
   * 将 Long 类型转换为 String 类型
   *
   * @param value 要转换的 Long 值
   * @return 转换后的 String 值，如果输入为 null 则返回 null
   */
  @Named("longToString(Value)")
  public static String longToString(Long value) {
    return value != null ? value.toString() : null;
  }

  /**
   * 将 Integer 类型转换为 String 类型
   *
   * @param value 要转换的 Integer 值
   * @return 转换后的 String 值，如果输入为 null 则返回 null
   */
  @Named("integerToString(Value)")
  public static String integerToString(Integer value) {
    return value != null ? value.toString() : null;
  }

  /**
   * 将 byte[] 类型转换为 String 类型
   *
   * @param value 要转换的 Integer 值
   * @return 转换后的 String 值，如果输入为 null 则返回 null
   */
  @Named("byteToString(Value)")
  public static String byteToString(byte[] value) {
    return value != null ? HexFormat.of().formatHex(value) : null;
  }
}