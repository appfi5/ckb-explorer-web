package com.ckb.explorer.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * @TableName udt_transactions
 */
@Data
public class UdtTransactions implements Serializable {
    private Long udtId;

    private Long ckbTransactionId;

    private Long blockTimeStamp;


    private static final long serialVersionUID = 1L;
}