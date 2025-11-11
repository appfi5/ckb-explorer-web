package com.ckb.explorer.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import org.apache.ibatis.type.ArrayTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.postgresql.jdbc.PgArray;
import org.postgresql.jdbc.BatchResultHandler;


/**
 * @TableName udts
 */
@Data
@TableName(autoResultMap = true)
public class Udts implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long typeScriptId;

    private byte[] typeScriptHash;

    private String fullName;

    private String symbol;

    private Integer decimal;

    private String description;

    private String iconFile;

    private String operatorWebsite;

    private Long addressesCount;

    private Long totalAmount;

    private Integer udtType;

    private Boolean published;

    private Date createdAt;

    private Date updatedAt;

    private Long blockTimestamp;

    private byte[] issuerAddress;

    private Long ckbTransactionsCount;

    private Long nrcFactoryCellId;

    private String displayName;

    private String uan;

    private Long h24CkbTransactionsCount;

    private String email;

    @TableField(typeHandler = ArrayTypeHandler.class)
    private String[] xudtTags;

    private static final long serialVersionUID = 1L;
}