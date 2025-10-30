package com.ckb.explorer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ckb.explorer.config.ScriptConfig;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.dto.UdtAddressCountDto;
import com.ckb.explorer.domain.dto.UdtH24TransactionsCountDto;
import com.ckb.explorer.domain.resp.UdtDetailResponse;
import com.ckb.explorer.domain.resp.UdtHolderAllocationsResponse;
import com.ckb.explorer.domain.resp.UdtsListResponse;
import com.ckb.explorer.entity.Script;
import com.ckb.explorer.entity.UdtHolderAllocations;
import com.ckb.explorer.enums.CellType;
import com.ckb.explorer.enums.LockType;
import com.ckb.explorer.mapper.Address24hTransactionMapper;
import com.ckb.explorer.mapper.UdtAccountsMapper;
import com.ckb.explorer.mapstruct.UdtHolderAllocationsConvert;
import com.ckb.explorer.service.ScriptService;
import com.ckb.explorer.service.UdtHolderAllocationsService;
import com.ckb.explorer.mapper.UdtHolderAllocationsMapper;
import com.ckb.explorer.util.I18n;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.nervos.ckb.utils.Numeric;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author dell
 * @description 针对表【udt_holder_allocations】的数据库操作Service实现
 * @createDate 2025-09-08 17:09:56
 */
@Service
@Slf4j
public class UdtHolderAllocationsServiceImpl extends ServiceImpl<UdtHolderAllocationsMapper, UdtHolderAllocations>
        implements UdtHolderAllocationsService {

    @Resource
    ScriptService scriptService;

    @Resource
    ScriptConfig lockScriptConfig;

    @Resource
    UdtAccountsMapper udtAccountsMapper;

    @Resource
    Address24hTransactionMapper address24hTransactionMapper;

    @Resource
    private I18n i18n;

    @Override
    public List<UdtHolderAllocationsResponse> findByTypeScriptHash(String typeScriptHash) {
        Script script = scriptService.findByScriptHash(typeScriptHash);
        if (script == null) {
            throw new ServerException(I18nKey.UDTS_NOT_FOUND_CODE, i18n.getMessage(I18nKey.UDTS_NOT_FOUND_MESSAGE));
        }
        QueryWrapper<UdtHolderAllocations> udtHolderAllocationsQueryWrapper = new QueryWrapper<>();
        udtHolderAllocationsQueryWrapper.eq("type_script_id", script.getId());
        List<UdtHolderAllocations> udtHolderAllocations = baseMapper.selectList(udtHolderAllocationsQueryWrapper);
        List<UdtHolderAllocationsResponse> udtHolderAllocationsResponses = UdtHolderAllocationsConvert.INSTANCE.udtHolderListtoResponse(udtHolderAllocations);
        udtHolderAllocationsResponses.forEach(udtHolderAllocationsResponse -> {
            udtHolderAllocationsResponse.setName(LockType.getValueByCode(udtHolderAllocationsResponse.getLockType()));
        });
        return udtHolderAllocationsResponses;
    }


    @Override
    public List<UdtsListResponse> udtListStatistic() {
        List<ScriptConfig.TypeScript> typeScripts = lockScriptConfig.getTypeScripts().stream()
                .filter(typeScript -> typeScript.getCellType() != null && CellType.valueOf(typeScript.getCellType()).isUdtType()).toList();

        List<UdtsListResponse> udtsListResponses = new ArrayList<>(typeScripts.size());

        List<UdtAddressCountDto> addressesCounts = super.baseMapper.getAddressNum();

        List<Long> typeScriptIds = addressesCounts.stream().map(UdtAddressCountDto::getTypeScriptId).collect(Collectors.toList());
        List<Script> scripts = scriptService.listByIds(typeScriptIds);
        List<UdtH24TransactionsCountDto> transactionsCounts = address24hTransactionMapper.getTransactionsCountByScriptIds(typeScriptIds);

        typeScripts.stream().forEach(typeScript -> {
            Long typeScriptId;
            Script scriptData = scripts.stream().filter(script -> Objects.equals(typeScript.getScriptHash(), Numeric.toHexString(script.getScriptHash())))
                    .findFirst().orElse(null);
            if (scriptData != null) {
                typeScriptId = scriptData.getId();
            } else {
                typeScriptId = null;
            }

            UdtsListResponse udtsListResponse = new UdtsListResponse();
            udtsListResponse.setTypeScriptHash(typeScript.getScriptHash());
            UdtAddressCountDto udtAddressCountDto = addressesCounts.stream().filter(addressesCount -> Objects.equals(addressesCount.getTypeScriptId(), typeScriptId)).findFirst().orElse(null);
            Long addressCount = udtAddressCountDto == null ? 0L : udtAddressCountDto.getAddressesCount();
            udtsListResponse.setAddressesCount(addressCount);
            UdtH24TransactionsCountDto udtH24TransactionsCountDto = transactionsCounts.stream().filter(transactionsCount -> Objects.equals(transactionsCount.getTypeScriptId(), typeScriptId)).findFirst().orElse(null);
            Long h24CkbTransactionsCount = udtH24TransactionsCountDto == null ? 0L : udtH24TransactionsCountDto.getH24CkbTransactionsCount();
            udtsListResponse.setH24CkbTransactionsCount(h24CkbTransactionsCount);
            udtsListResponses.add(udtsListResponse);
        });
        return udtsListResponses;
    }

    @Override
    public UdtDetailResponse findDetailByTypeHash(String typeHash) {
        Script script = scriptService.findByScriptHash(typeHash);
        if (script == null) {
            throw new ServerException(I18nKey.UDTS_NOT_FOUND_CODE, i18n.getMessage(I18nKey.UDTS_NOT_FOUND_MESSAGE));
        }
        Long holdersCount = super.baseMapper.selectHolderCountByTypeScriptId(script.getId());
        holdersCount = holdersCount == null ? 0 : holdersCount;
        UdtDetailResponse udtDetailResponse = new UdtDetailResponse();
        udtDetailResponse.setHoldersCount(holdersCount);
        BigInteger totalAmount = udtAccountsMapper.getTotalAmountByTypeScriptId(script.getId());
        totalAmount = totalAmount == null ? BigInteger.ZERO : totalAmount;
        udtDetailResponse.setTotalAmount(totalAmount);
        return udtDetailResponse;
    }
}




