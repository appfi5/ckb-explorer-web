package com.ckb.explorer.util;

public class FieldNameConverter {
  // 驼峰转下划线
  public static String camelToSnake(String camelCase) {
    if (camelCase == null || camelCase.isEmpty()) {
      return camelCase;
    }
    // 在大写字母前加下划线，然后转小写
    return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
  }
}
