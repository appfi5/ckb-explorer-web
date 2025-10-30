package com.ckb.explorer.domain.dto;

import lombok.Data;

@Data
public class BlockDaoDto {

  private Long blockNumber;

  private byte[] dao;
}
