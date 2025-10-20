package com.ckb.explorer.domain.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UdtDailyStatisticsResponse {
  @JsonSerialize(using = ToStringSerializer.class)
  private Integer ckbTransactionsCount;

  @JsonSerialize(using = ToStringSerializer.class)
  private Integer holdersCount;

  @JsonSerialize(using = ToStringSerializer.class)
  private Long createdAtUnixtimestamp;
}
