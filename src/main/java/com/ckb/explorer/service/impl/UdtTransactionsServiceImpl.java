package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.domain.dto.CellInputDto;
import com.ckb.explorer.domain.dto.CellOutputDto;
import com.ckb.explorer.domain.resp.CellInputResponse;
import com.ckb.explorer.domain.resp.CellOutputResponse;
import com.ckb.explorer.domain.resp.UdtTransactionPageResponse;
import com.ckb.explorer.entity.UdtTransactions;
import com.ckb.explorer.entity.Udts;
import com.ckb.explorer.mapper.CkbTransactionMapper;
import com.ckb.explorer.mapper.InputMapper;
import com.ckb.explorer.mapper.OutputMapper;
import com.ckb.explorer.mapstruct.CellInputConvert;
import com.ckb.explorer.mapstruct.CellOutputConvert;
import com.ckb.explorer.service.UdtTransactionsService;
import com.ckb.explorer.mapper.UdtTransactionsMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author dell
* @description 针对表【udt_transactions】的数据库操作Service实现
* @createDate 2025-09-09 15:20:04
*/
@Service
public class UdtTransactionsServiceImpl extends ServiceImpl<UdtTransactionsMapper, UdtTransactions>
    implements UdtTransactionsService{



    @Resource
    CkbTransactionMapper ckbTransactionMapper;

    @Resource
    private InputMapper inputMapper;

    @Resource
    private OutputMapper outputMapper;

    @Override
    public Page<UdtTransactionPageResponse> page(String typeScriptHash, Integer page, Integer pageSize){
       return null;
    }


}




