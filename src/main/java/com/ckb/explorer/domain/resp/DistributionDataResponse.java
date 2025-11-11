package com.ckb.explorer.domain.resp;

import com.ckb.explorer.domain.dto.AddressBalanceDistributionDto;
import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.ckb.explorer.entity.RollingAvgBlockTime;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DistributionDataResponse extends BaseResponse<Long> {
  private String type = "distribution_data";

  private List<String[]> addressBalanceDistribution;

  private List<String[]> blockTimeDistribution;

  private List<String[]> epochTimeDistribution;

  private List<RollingAvgBlockTime> averageBlockTime;

  private Object minerAddressDistribution;
}
