package com.ckb.explorer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UdtType {

    SUDT(3, "sudt"),
    M_NFT_TOKEN(6, "m_nft_token"),
    NRC_721_TOKEN(7, "nrc_721_token"),
    SPORE_CELL(12, "spore_cell"),
    OMIGA_INSCRIPTION(14, "omiga_inscription"),
    XUDT(15, "xudt"),
    XUDT_COMPATIBLE(17, "xudt_compatible"),
    DID_CELL(18, "did_cell"),
    SSRI(20, "ssri");

    private final int code;
    private final String name; // 原始名称（与Ruby枚举键名一致）
    public static UdtType valueOf(int code) {
        for (UdtType type : UdtType.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("不支持的UDT类型代码: " + code);
    }

  public static String getNameByValue(int value) {
    return valueOf(value).getName();

  }
}
