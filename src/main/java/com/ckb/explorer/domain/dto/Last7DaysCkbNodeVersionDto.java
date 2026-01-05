package com.ckb.explorer.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Last7DaysCkbNodeVersionDto {
  private byte[] version ;

  private Integer count;
}
