package com.ckb.explorer.domain.dto;

import com.ckb.explorer.domain.resp.CellDependencyResponse;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class PendingTransactionDto {
  @Id
  private String txHash;

  private String version;

  private Integer inputCount;

  private Integer outputCount;

  private byte[] witnesses;

  private byte[] headerDeps;

  private Long bytes;

  private Integer status;

  private Long createdAt;

  private String updatedAt;

  private List<CellDependencyResponse> cellDeps;
}
