package com.ckb.explorer.domain.resp;

import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlockListResponse extends BaseResponse<Long> {

  private String minerHash;
  private Long number;
  private Long timestamp;
  private Long reward;
  private Integer transactionsCount;
  private Integer liveCellChanges;
}
