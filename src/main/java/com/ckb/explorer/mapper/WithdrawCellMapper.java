package com.ckb.explorer.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ckb.explorer.entity.WithdrawCell;

/**
 * @description 针对表【withdraw_cell】的数据库操作Mapper
 * @Entity com.ckb.explore.worker.entity.WithdrawCell
 */
@DS("risingwave")
public interface WithdrawCellMapper extends BaseMapper<WithdrawCell> {


}