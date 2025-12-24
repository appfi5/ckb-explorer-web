package com.ckb.explorer.domain.resp;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NftItemDetailResponse {


    private Long id;

    @Schema(description="持有者")
    private String owner;

    @Schema(description="tokenId")
    private String tokenId;

    @Schema(description="cell data")
    private String data;

    @Schema(description="collection名称")
    private String collectionName;

    @Schema(description="collection id")
    private Long collectionId;

    private String creator;

    private String iconUrl;

}
