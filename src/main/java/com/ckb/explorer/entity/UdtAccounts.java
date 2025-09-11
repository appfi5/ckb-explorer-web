package com.ckb.explorer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigInteger;
import lombok.Data;

/**
 * @TableName udt_accounts
 */
@Data
public class UdtAccounts implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long typeScriptId;

    private String nftTokenId;

    private BigInteger amount;

    private Long lockScriptId;


    private static final long serialVersionUID = 1L;
}