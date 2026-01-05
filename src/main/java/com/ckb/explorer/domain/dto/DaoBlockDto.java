package com.ckb.explorer.domain.dto;

import lombok.Data;

@Data
public class DaoBlockDto {
  Long blockNumber;

  Long timestamp;

  byte[] Dao;
}
