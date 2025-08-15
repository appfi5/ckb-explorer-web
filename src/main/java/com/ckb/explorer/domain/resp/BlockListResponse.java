package com.ckb.explorer.domain.resp;

import lombok.Data;

@Data
public class BlockListResponse {

  private String miner_hash;
  private Long number;
  private Long timestamp;
  private String reward;
  private Integer ckb_transactions_count;
  private Integer live_cell_changes;
}
