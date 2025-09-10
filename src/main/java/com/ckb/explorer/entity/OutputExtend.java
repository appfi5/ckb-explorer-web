package com.ckb.explorer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigInteger;
import lombok.Data;

/**
 * @TableName output_extend
 */
@Data
public class OutputExtend implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long outputId;

    private Integer cellType;

    private BigInteger udtAmount;

    private static final long serialVersionUID = 1L;
}