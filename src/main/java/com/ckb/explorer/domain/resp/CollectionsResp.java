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
public class CollectionsResp {



    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "描述")
    private  String description;

    @Schema(description = "minted数量")
    private Long itemsCount;

    @Schema(description = "holder数量")
    private Long holdersCount;

    @Schema(description = "近24小时交易数量")
    private Long h24CkbTransactionsCount;

    @Schema(description = "标签")
    private String[] tags;

    @Schema(description = "创建时间")
    private Long blockTimestamp;

    @Schema(description = "创建人")
    private String creator;

    @Schema(description = "clusterId")
    private String clusterId;

    @Schema(description = "typeScriptHash")
    private String typeScriptHash;

    @Schema(description = "cell id")
    private Long cellId;






}
