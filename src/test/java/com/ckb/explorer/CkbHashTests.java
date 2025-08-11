package com.ckb.explorer;

import com.ckb.explorer.utils.CkbArrayHashType;
import com.ckb.explorer.utils.CkbHashType;
import java.util.HexFormat;
import java.util.List;

public class CkbHashTests {

  public static void main(String[] args) {
    CkbHashType hashUtil = new CkbHashType("0x");

    var hashStr = "0xcead9dd0a8906660a867eb0f51645b3ece538e81b6e17df7959cb46dc58b714b";
    // 示例：字符串 -> 字节数组
    byte[] bytes = hashUtil.serialize(hashStr);
    System.out.println(HexFormat.of().formatHex(bytes)); // 输出: cead9dd0a8906660a867eb0f51645b3ece538e81b6e17df7959cb46dc58b714b

    // 示例：字节数组 -> 字符串
    String hashStrResult = hashUtil.deserialize(bytes);
    System.out.println(hashStrResult); // 输出: 0xcead9dd0a8906660a867eb0f51645b3ece538e81b6e17df7959cb46dc58b714b



    CkbArrayHashType arrayUtil = new CkbArrayHashType("0x", 10); // 哈希长度为 3 字节

    // 示例：字符串数组 -> 字节数组
    List<String> input = List.of("0x732d2c264a5e2b639238", "0x5ca3b745d7651706b1d9","0x0db7512ee4bf52e790c5");
    byte[] arrayBytes = arrayUtil.serialize(input);
    System.out.println(HexFormat.of().formatHex(arrayBytes)); // 输出: ...

    // 示例：字节数组 -> 字符串数组

    List<String> hashes = arrayUtil.deserialize(arrayBytes);
    System.out.println(hashes); // 输出: [0x732d2c264a5e2b639238, 0x5ca3b745d7651706b1d9...]

    byte[] bytes1 = new byte[]{115, 45, 44, 38, 74, 94, 43, 99, -110, 56};
    byte[] bytes2 = new byte[]{92, -93, -73, 69, -41, 101, 23, 6, -79, -39};
    byte[] bytes3 = new byte[]{13, -73, 81, 46, -28, -65, 82, -25, -112, -59};
    String str1 = HexFormat.of().formatHex(bytes1);
    System.out.println(str1);
    String str2 = HexFormat.of().formatHex(bytes2);
    System.out.println(str2);
    String str3 = HexFormat.of().formatHex(bytes3);
    System.out.println(str3);
  }
}
