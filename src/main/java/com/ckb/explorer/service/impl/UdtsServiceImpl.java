package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.req.UdtsPageReq;
import com.ckb.explorer.domain.resp.ScriptResponse;
import com.ckb.explorer.domain.resp.UdtDetailResponse;
import com.ckb.explorer.domain.resp.XudtsPageResponse;
import com.ckb.explorer.entity.Script;
import com.ckb.explorer.entity.Udts;
import com.ckb.explorer.enums.HashType;
import com.ckb.explorer.enums.UdtType;
import com.ckb.explorer.mapper.ScriptMapper;
import com.ckb.explorer.mapper.UdtHolderAllocationsMapper;
import com.ckb.explorer.mapstruct.UdtsConvert;
import com.ckb.explorer.service.UdtsService;
import com.ckb.explorer.mapper.UdtsMapper;
import com.ckb.explorer.util.I18n;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.nervos.ckb.utils.Numeric;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.ckb.explorer.constants.TokenType.TOKEN_TYPE_SUDT;
import static com.ckb.explorer.constants.TokenType.TOKEN_TYPE_XUDT;

/**
* @author dell
* @description 针对表【udts】的数据库操作Service实现
* @createDate 2025-09-05 11:01:18
*/
@Service
@Slf4j
public class UdtsServiceImpl extends ServiceImpl<UdtsMapper, Udts>
    implements UdtsService{

    @Resource
    private I18n i18n;

    @Resource
    ScriptMapper scriptMapper;
    @Resource
    UdtHolderAllocationsMapper udtHolderAllocationsMapper;




    @Override
    public Page<Udts> getUdtsPageBy(UdtsPageReq req){

        Page<Udts> page = new Page<>(req.getPage(), req.getPageSize());

        QueryWrapper<Udts> queryWrapper = condition(req);

        page =  page(page,queryWrapper);
        return  page;

    }


    private QueryWrapper<Udts> condition(UdtsPageReq req){
        List<Integer> udtTypes = Arrays.asList(UdtType.XUDT.getCode(),UdtType.XUDT_COMPATIBLE.getCode(),UdtType.SUDT.getCode(),UdtType.SSRI.getCode());

        QueryWrapper<Udts> udtsQueryWrapper = new QueryWrapper<>();
        log.info("haslenth {}",StringUtils.hasLength(req.getTags()));

        if(req.isUnion()){
            if(StringUtils.hasLength(req.getTags())){
                udtsQueryWrapper.apply(StringUtils.hasLength(req.getTags())," xudt_tags && array["+Arrays.stream(req.getTags().split(",")).map(v -> "'" + v + "'")
                        .collect(Collectors.joining(","))+"]::varchar[] " );

                udtsQueryWrapper.apply(StringUtils.hasLength(req.getTags())," xudt_tags @> array["+Arrays.stream(req.getTags().split(",")).map(v -> "'" + v + "'")
                        .collect(Collectors.joining(","))+"]::varchar[] ");
            }
        }
        if(TOKEN_TYPE_XUDT.equals(req.getTokenType())){
            udtTypes = Arrays.asList(UdtType.XUDT.getCode(),UdtType.XUDT_COMPATIBLE.getCode());
        }else if(TOKEN_TYPE_SUDT.equals(req.getTokenType())){
            udtTypes = Arrays.asList(UdtType.SUDT.getCode());
        }

        udtsQueryWrapper.in("udt_type",udtTypes);

        if(req.getPublished()!=null){
            udtsQueryWrapper.eq("published",req.getPublished());
        }

        if(StringUtils.hasLength(req.getSort())){
            String[] sorts= req.getSort().split(".",2);
            String sortParam = sorts[0];
            String orderParam = sorts[1];
            if(StringUtils.isEmpty(sortParam)|| !Pattern.compile("/^(block_timestamp|addresses_count|h24_ckb_transactions_count)$/i").matcher(orderParam).matches()){
                sortParam = "id";
            }
            if(StringUtils.isEmpty(orderParam)|| !Pattern.compile("/^(asc|desc)$/i").matcher(orderParam).matches()){
                orderParam = "asc";
            }
            udtsQueryWrapper.orderBy(true,"asc".equals(orderParam),sortParam);
        }else {
            udtsQueryWrapper.orderByAsc("id");
        }

        return udtsQueryWrapper;
    }

    @Override
    public UdtDetailResponse findUdtDetailByTypeScriptHash(String typeScriptHash){
          Udts udts = findUdtsByTypeScriptHash(typeScriptHash);
          UdtDetailResponse udtDetailResponse = UdtsConvert.INSTANCE.udtsToUdtDetailResponse(udts);
          Script script = scriptMapper.selectById(udts.getTypeScriptId());
          ScriptResponse scriptResponse = new ScriptResponse();
          scriptResponse.setArgs(Numeric.toHexString(script.getArgs()));
          scriptResponse.setCodeHash(Numeric.toHexString(script.getScriptHash()));
          scriptResponse.setHashType(HashType.getValueByCode(script.getHashType()));
          udtDetailResponse.setTypeScript(scriptResponse);
          Long holdersCount = udtHolderAllocationsMapper.selectHolderCountByTypeScriptId(udts.getTypeScriptId());
          udtDetailResponse.setHoldersCount(holdersCount);
          return udtDetailResponse;
    }

    @Override
    public Udts findUdtsByTypeScriptHash(String typeScriptHash) {
        QueryWrapper<Udts> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_script_hash", Numeric.hexStringToByteArray(typeScriptHash));
        Udts udts = getOne(queryWrapper);
        if(udts==null){
            throw new ServerException(I18nKey.UDTS_NOT_FOUND_CODE, i18n.getMessage(I18nKey.UDTS_NOT_FOUND_MESSAGE));
        }
        return  udts;
    }


}




