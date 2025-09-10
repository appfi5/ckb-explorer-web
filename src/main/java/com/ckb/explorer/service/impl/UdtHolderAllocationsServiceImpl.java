package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.config.ScriptConfig;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.resp.UdtHolderAllocationsResponse;
import com.ckb.explorer.entity.Script;
import com.ckb.explorer.entity.UdtHolderAllocations;
import com.ckb.explorer.mapstruct.UdtsConvert;
import com.ckb.explorer.service.ScriptService;
import com.ckb.explorer.service.UdtHolderAllocationsService;
import com.ckb.explorer.mapper.UdtHolderAllocationsMapper;
import com.ckb.explorer.util.I18n;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author dell
* @description 针对表【udt_holder_allocations】的数据库操作Service实现
* @createDate 2025-09-08 17:09:56
*/
@Service
public class UdtHolderAllocationsServiceImpl extends ServiceImpl<UdtHolderAllocationsMapper, UdtHolderAllocations>
    implements UdtHolderAllocationsService{

    @Resource
    ScriptService scriptService;

    @Resource
    ScriptConfig lockScriptConfig;

    @Resource
    private I18n i18n;

    @Override
    public List<UdtHolderAllocationsResponse> findByTypeScriptHash(String typeScriptHash){
         Script script = scriptService.findByScriptHash(typeScriptHash);
         if(script==null){
             throw new ServerException(I18nKey.UDTS_NOT_FOUND_CODE, i18n.getMessage(I18nKey.UDTS_NOT_FOUND_MESSAGE));
         }
         QueryWrapper<UdtHolderAllocations> udtHolderAllocationsQueryWrapper = new QueryWrapper<>();
         udtHolderAllocationsQueryWrapper.eq("type_script_id",script.getId());
         List<UdtHolderAllocations> udtHolderAllocations = baseMapper.selectList(udtHolderAllocationsQueryWrapper);
         List<UdtHolderAllocationsResponse> udtHolderAllocationsResponses = UdtsConvert.INSTANCE.udtHolderListtoResponse(udtHolderAllocations);
         udtHolderAllocationsResponses.forEach(udtHolderAllocationsResponse -> {
             ScriptConfig.LockScript lockScript = lockScriptConfig.getLockScriptByCodeHash(udtHolderAllocationsResponse.getLockCodeHash());
             if(lockScript!=null){
                 udtHolderAllocationsResponse.setName(lockScript.getName());
                 udtHolderAllocationsResponse.setHashType(lockScript.getHashType());
             }
         });
         return udtHolderAllocationsResponses;
     }
}




