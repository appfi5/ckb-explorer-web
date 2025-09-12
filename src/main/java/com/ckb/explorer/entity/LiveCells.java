package com.ckb.explorer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigInteger;
import lombok.Data;

/**
 * @TableName live_cells
 */
@Data
public class LiveCells implements Serializable {


    @TableId(type = IdType.AUTO)
    private Long id;

    private Long outputId;

    private BigInteger capacity;

    private Long lockScriptId;

    private Long typeScriptId;

    private Integer cellType;

    private BigInteger udtAmount;

    private BigInteger occupiedCapacity;

    private Long blockNumber;

    private Long blockTimestamp;

    private byte[] txHash;

    private Long txId;


    private byte[] data;

    private static final long serialVersionUID = 1L;
}