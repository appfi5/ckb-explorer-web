package com.ckb.explorer.domain.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UdtHolderAllocationsResponse  {


    private String name;

    private Integer lockType;

    private Integer holderCount;

    private String hashType;






}