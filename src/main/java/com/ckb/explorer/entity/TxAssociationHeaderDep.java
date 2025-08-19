package com.ckb.explorer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tx_association_header_dep")
public class TxAssociationHeaderDep {

  @TableId(type = IdType.AUTO)
  private Long id;

  private Long txId;

  private Long blockId;
}