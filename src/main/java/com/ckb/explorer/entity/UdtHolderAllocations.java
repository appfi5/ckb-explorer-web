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


    private Long typeScriptId;

    private Integer holderCount;

    private Integer lockType;

    private static final long serialVersionUID = 1L;
}