package com.ckb.explorer.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
 import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionsDto {



     private Long id;

     private String name;

     private  String description;

     private Long itemsCount;

     private Long holdersCount;

     private Long h24CkbTransactionsCount;

     private String[] tags;

     private Long blockTimestamp;

     private String creator;

     private byte[] args;






}
