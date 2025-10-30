package com.ckb.explorer.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NftHolderDto {


    private Long lockScriptId;

    private Long holdersCount ;

    private String addressHash;

}
