package com.ckb.explorer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class UdtDailyStatistics implements Serializable {

  @TableId(type = IdType.AUTO)
  private Long id;

  private Long scriptId;

  private Integer ckbTransactionsCount;

  private Integer holdersCount;

  private Long createdAtUnixtimestamp;

  private Date createdAt;

  private Date updatedAt;

  private static final long serialVersionUID = 1L;
}
