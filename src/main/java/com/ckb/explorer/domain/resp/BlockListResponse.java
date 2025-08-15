package com.ckb.explorer.domain.resp;

import lombok.Data;

@Data
public class BlockListResponse {

  private String miner_hash;
  private String number;
  private String timestamp;
  private String reward;
  private String transactions_count;
  private String live_cell_changes;
}
