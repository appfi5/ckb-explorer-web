package com.ckb.explorer.domain.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListStringWrapper {
  List<String[]> data;
}
