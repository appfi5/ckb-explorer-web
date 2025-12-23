package com.ckb.explorer.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tx_association_cell_dep")
public class PendingTxAssociationCellDep {

  private byte[] txHash;

  private Integer index;

  private byte[] outpointTxHash;

  private Integer outpointIndex;

  private Short depType;
}