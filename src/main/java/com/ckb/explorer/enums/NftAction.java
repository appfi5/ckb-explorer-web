package com.ckb.explorer.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.Locale;

@Getter
@AllArgsConstructor
public enum NftAction {


    MINT(0, "Mint"),
    TRANSFER(1, "Transfer"),
    BURN(2, "Burn");

    private final int code;
    private final String value;


    public static String getValueByCode(Integer code){
        if(code == null){
            return null;
        }
        NftAction[] nftActions = NftAction.values();
        for (NftAction nftAction : nftActions) {
            if(nftAction.getCode() == code.intValue()){
                return nftAction.getValue();
            }
        }
        return TRANSFER.value;
    }

    public static int getCodeByValue(String value){
        if(!StringUtils.hasLength(value)){
            return TRANSFER.code;
        }
        NftAction[] nftActions = NftAction.values();
        for (NftAction nftAction : nftActions) {
            if(nftAction.getValue().toLowerCase(Locale.ROOT).equals(value.toLowerCase(Locale.ROOT))){
                return nftAction.getCode();
            }
        }
        return TRANSFER.code;
    }

}
