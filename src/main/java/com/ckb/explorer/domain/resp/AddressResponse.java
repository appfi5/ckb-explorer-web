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
  private Long balance = 0L;

  private Long transactionsCount = 0L;

  /**
   * DAO 押金
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long daoDeposit = 0L; // 一期不做DAO相关

  /**
   * 利息
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long interest = 0L; // 一期不做DAO相关

  private Long liveCellsCount=0L;

  // private Long minedBlocksCount=0L; // 去掉

  private Long averageDepositTime; // 一期不做DAO相关

  private List<UdtAccountResponse> udtAccounts;// TODO 现在还没有

  private LockScriptResponse lockScript;

  /**
   * DAO 补偿
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private Long daoCompensation = 0L;// 一期不做DAO相关

  @JsonSerialize(using = ToStringSerializer.class)
  private Long balanceOccupied = 0L; //

  private String bitcoinAddressHash; // 一期不做rgb++ 比特币相关

}
