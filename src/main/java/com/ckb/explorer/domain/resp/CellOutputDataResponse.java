package com.ckb.explorer.domain.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Base64;
import lombok.NoArgsConstructor;

/**
 * CellOutputDataSerializer，用于序列化CellOutput的数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CellOutputDataResponse {

    @JsonProperty("data")
    private String data;

}