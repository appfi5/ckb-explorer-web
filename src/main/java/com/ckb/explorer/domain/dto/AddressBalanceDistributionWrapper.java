package com.ckb.explorer.domain.dto;

import java.util.List;

public class AddressBalanceDistributionWrapper {
  private List<AddressBalanceDistributionDto> data; // 实际数据列表

  // Getter + Setter
  public List<AddressBalanceDistributionDto> getData() { return data; }
  public void setData(List<AddressBalanceDistributionDto> data) { this.data = data; }
}
