package com.ckb.explorer.util;

import com.ckb.explorer.enums.NetWorkEnums;
import jakarta.annotation.PostConstruct;
import java.util.HexFormat;
import org.mapstruct.Named;
import org.nervos.ckb.type.Script;
import org.nervos.ckb.type.Script.HashType;
import org.nervos.ckb.utils.address.Address;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TypeConversionUtil {

  @Value("${ckb.netWork}")
  private Integer netWork;

  // 静态变量
  private static Integer staticNetWork;

  // 在 Bean 初始化时赋值给静态变量
  @PostConstruct
  public void init() {
    staticNetWork = this.netWork;
  }

  // 提供静态方法访问
  public static Integer getNetwork() {
    return staticNetWork;
  }

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

  /**
   * 将 byte[] 类型的lockScript转换为 String 类型的Address
   *
   * @param value 要转换的 Integer 值
   * @return 转换后的 String 值，如果输入为 null 则返回 null
   */
  @Named("lockScriptToAddress(Value)")
  public static String lockScriptToAddress(byte[] value) {
    var net = NetWorkEnums.getNet(getNetwork());
    org.nervos.ckb.type.concrete.Script script = org.nervos.ckb.type.concrete.Script.builder(value).build();
    var scriptNew = new Script(script.getCodeHash().getItems(),script.getArgs().getItems(),
        HashType.unpack(script.getHashType()));
    var addrResult = new Address(scriptNew, net);
    return addrResult.encode();
  }

  /**
   * 将 byte[] 类型转换为 String 类型Hash
   *
   * @param value 要转换的 Integer 值
   * @return 转换后的 String 值，如果输入为 null 则返回 null
   */
  @Named("byteToStringHash(Value)")
  public static String byteToStringHash(byte[] value) {
    return value != null ? "0x"+HexFormat.of().formatHex(value) : null;
  }
}