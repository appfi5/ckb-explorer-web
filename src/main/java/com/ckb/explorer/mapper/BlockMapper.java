package com.ckb.explorer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ckb.explorer.entity.Block;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface BlockMapper extends BaseMapper<Block> {

}