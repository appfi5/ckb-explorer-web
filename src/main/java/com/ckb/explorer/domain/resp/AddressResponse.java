package com.ckb.explorer.domain.resp;

import com.ckb.explorer.domain.resp.base.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.math.BigDecimal;
import java.math.BigInteger;
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
  private BigDecimal balance = BigDecimal.ZERO;

  /**
   * DAO 押金
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private BigInteger daoDeposit = BigInteger.ZERO;

  /**
   * 利息
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private BigInteger interest = BigInteger.ZERO;

  private Long liveCellsCount=0L;

  private LockScriptResponse lockScript;

  /**
   * DAO 补偿 拆成下面两个字段
   */
  //@JsonSerialize(using = ToStringSerializer.class)
  //private Long daoCompensation = 0L;//

  /**
   * 阶段1未领取的补偿
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private BigInteger phase1UnClaimedCompensation = BigInteger.ZERO;

  /**
   * Deposit阶段未提取补贴
   */
  @JsonSerialize(using = ToStringSerializer.class)
  private BigInteger depositUnmadeCompensation = BigInteger.ZERO;

  @JsonSerialize(using = ToStringSerializer.class)
  private BigDecimal balanceOccupied = BigDecimal.ZERO; //

  private String bitcoinAddressHash; // 一期不做rgb++ 比特币相关

}
