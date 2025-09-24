package com.ckb.explorer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName address_24h_transaction
 */
@Data
public class Address24hTransaction implements Serializable {


    private Long lockScriptId;

    private Long ckbTransactionId;

    private Long blockTimestamp;

    private Long typeScriptId;

    private static final long serialVersionUID = 1L;
}