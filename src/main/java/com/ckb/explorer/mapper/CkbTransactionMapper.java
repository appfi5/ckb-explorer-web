package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ckb.explorer.entity.AccountBook;
import com.ckb.explorer.entity.CkbTransaction;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CkbTransactionMapper extends BaseMapper<CkbTransaction> {

}