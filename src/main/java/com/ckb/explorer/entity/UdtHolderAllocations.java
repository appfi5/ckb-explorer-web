package com.ckb.explorer.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @TableName udt_holder_allocations
 */
@Data
public class UdtHolderAllocations implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long typeScriptId;

    private Integer holderCount;

    private byte[] lockCodeHash;

    private static final long serialVersionUID = 1L;
}