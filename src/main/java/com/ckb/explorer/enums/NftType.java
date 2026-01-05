package com.ckb.explorer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.Locale;

@Getter
@AllArgsConstructor
public enum NftType {
    DOB(0,"spore"),
    M_NFT(1,"m_nft");
    private final int code;
    private final String value;


    public static String getValueByCode(Integer code){
        if(code == null){
            return null;
        }
        NftType[] nftTypes = NftType.values();
        for (NftType nftType : nftTypes) {
            if(nftType.getCode() == code.intValue()){
                return nftType.getValue();
            }
        }
        return DOB.value;
    }

    public static int getCodeByValue(String value){
        if(!StringUtils.hasLength(value)){
            return DOB.code;
        }
        NftType[] nftTypes = NftType.values();
        for (NftType nftType : nftTypes) {
            if(nftType.getValue().toLowerCase(Locale.ROOT).equals(value.toLowerCase(Locale.ROOT))){
                return nftType.getCode();
            }
        }
        return DOB.code;
    }


    public static Integer getCodeByValueAllowNull(String value){
        if(!StringUtils.hasLength(value)){
            return null;
        }
        NftType[] nftTypes = NftType.values();
        for (NftType nftType : nftTypes) {
            if(nftType.getValue().toLowerCase(Locale.ROOT).equals(value.toLowerCase(Locale.ROOT))){
                return nftType.getCode();
            }
        }
        return null;
    }

}
