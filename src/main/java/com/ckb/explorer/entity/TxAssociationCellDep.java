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

  private Long tx_id;

  private Integer index;

  private byte[] outpoint_tx_hash;

  private Integer outpoint_index;

  private Long output_id;

  private Short dep_type;
}