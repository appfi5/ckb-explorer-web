package com.ckb.explorer.domain.resp;

import lombok.Data;

import java.io.Serializable;


@Data
public class UdtHolderAllocationsResponse  {


    private String name;

    private Integer lockType;

    private Integer holderCount;

    private String hashType;






}