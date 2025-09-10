package com.ckb.explorer.domain.resp;

import lombok.Data;

import java.io.Serializable;


@Data
public class UdtHolderAllocationsResponse  {


    private String name;

    private String lockCodeHash;

    private Integer holderCount;

    private String hashType;






}