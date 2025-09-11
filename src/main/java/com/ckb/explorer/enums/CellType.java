package com.ckb.explorer.enums;


import java.util.EnumSet;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CellType {
  NORMAL(0, "normal"),
  NERVOS_DAO_DEPOSIT(1, "nervos_dao_deposit"),
  NERVOS_DAO_WITHDRAWING(2, "nervos_dao_withdrawing"),
  UDT(3, "udt"),
  M_NFT_ISSUER(4, "m_nft_issuer"),
  M_NFT_CLASS(5, "m_nft_class"),
  M_NFT_TOKEN(6, "m_nft_token"),
  NRC_721_TOKEN(7, "nrc_721_token"),
  NRC_721_FACTORY(8, "nrc_721_factory"),
  COTA_REGISTRY(9, "cota_registry"),
  COTA_REGULAR(10, "cota_regular"),
  SPORE_CLUSTER(11, "spore_cluster"),
  SPORE_CELL(12, "spore_cell"),
  OMIGA_INSCRIPTION_INFO(13, "omiga_inscription_info"),
  OMIGA_INSCRIPTION(14, "omiga_inscription"),
  XUDT(15, "xudt"),
  UNIQUE_CELL(16, "unique_cell"),
  XUDT_COMPATIBLE(17, "xudt_compatible"),
  DID_CELL(18, "did_cell"),
  STABLEPP_POOL(19, "stablepp_pool"),
  SSRI(20, "ssri");
    private int value;
    private String name; // 原始名称（与Ruby枚举键名一致）
    public static CellType valueOf(int value) {
        for (CellType type : CellType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("不支持的CellType代码: " + value);
    }

  public static String getNameByValue(int value) {
      return valueOf(value).getName();
    }

    private   static EnumSet<CellType> udtCellType = EnumSet.of(UDT,XUDT,XUDT_COMPATIBLE);

    public   boolean isUdtType(){
        return udtCellType.contains(this);
    }
}
