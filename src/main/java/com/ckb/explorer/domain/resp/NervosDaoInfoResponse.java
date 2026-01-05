package com.ckb.explorer.domain.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NervosDaoInfoResponse {

  // ----------------------nervos_dao_withdrawing,nervos_dao_deposit------------------------------------------
  @JsonSerialize(using = ToStringSerializer.class)
  private Long compensationStartedBlockNumber;

  @JsonSerialize(using = ToStringSerializer.class)
  private Long compensationStartedTimestamp;

  @JsonSerialize(using = ToStringSerializer.class)
  private Long compensationEndedBlockNumber;

  @JsonSerialize(using = ToStringSerializer.class)
  private Long compensationEndedTimestamp;

  @JsonSerialize(using = ToStringSerializer.class)
  private BigInteger interest;

  @JsonSerialize(using = ToStringSerializer.class)
  private Long lockedUntilBlockNumber;

  @JsonSerialize(using = ToStringSerializer.class)
  private Long lockedUntilBlockTimestamp;
}
