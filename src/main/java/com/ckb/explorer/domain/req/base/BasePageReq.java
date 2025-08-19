package com.ckb.explorer.domain.req.base;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Schema(description = "分页请求通用参数")
public class BasePageReq implements Serializable {
  /**
   * 当前页面数据量
   */
  @Schema(description = "当前页面数据量", example = "10")
  @Min(value = 1, message = "Params pageSize should be an integer greater than 0")
  private int pageSize = 10;
  /**
   * 当前页码
   */
  @Schema(description = "当前页码", example = "1")
  @Min(value = 1, message = "Params page should be an integer greater than 0")
  private int page = 1;

  /**
   * 排序字段
   */
  @Schema(description = "排序字段")
  private String sort;

}
