package com.ckb.explorer.domain.resp;

import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressResponse extends BaseResponse<Long> {
  private String type = "address";
  /**
   * 地址
   */
  private String addressHash;

  @JsonSerialize(using = ToStringSerializer.class)
  private Long balance = 0L;  // TODO 后面从统计表取

  private Long transactionsCount = 0L; // TODO 统计表取

  /**
   * DAO 押金
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long daoDeposit = 0L; // TODO 现在没有

  /**
   * 利息
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long interest = 0L; // TODO 现在没有

  private Long liveCellsCount=0L; // TODO 统计表取

  private Long minedBlocksCount=0L; // TODO 统计表取

  private Long averageDepositTime; // TODO 也是dao相关的，现在没有

  private List<UdtAccountResponse> udtAccounts;// TODO 现在还没有

  private LockScriptResponse lockScript;

  /**
   * DAO 补偿
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long daoCompensation = 0L;// TODO 也是dao相关的，现在没有

  @JsonSerialize(using = ToStringSerializer.class)
  private Long balanceOccupied = 0L; // TODO 统计表取

  private String bitcoinAddressHash; // TODO rgb++相关，现在没有

}
