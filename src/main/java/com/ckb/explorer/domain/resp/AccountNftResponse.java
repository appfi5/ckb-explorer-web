package com.ckb.explorer.domain.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountNftResponse {

    @Schema(description = "nft collection 名称")
    private String collectionName;

    @Schema(description = "nft collection typeHash")
    private String collectionTypeHash;

    @Schema(description = "tokenId")
    private String tokenId;

    @Schema(description = "nft图片信息")
    private String nftIconFile;

    @Schema(description = "cell id")
    private Long cellId;

 }
