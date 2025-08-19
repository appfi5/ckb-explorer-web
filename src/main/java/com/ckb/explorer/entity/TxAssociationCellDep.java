package com.ckb.explorer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tx_association_cell_dep")
public class TxAssociationCellDep {

  @TableId(type = IdType.AUTO)
  private Long id;

  private Long txId;

  private Integer index;

  private byte[] outpointTxHash;

  private Integer outpointIndex;

  private Long outputId;

  private Short depType;
}