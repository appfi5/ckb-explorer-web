package com.ckb.explorer.controller;

import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.resp.IndexStatisticResponse;
import com.ckb.explorer.domain.resp.StatisticResponse;
import com.ckb.explorer.facade.IStatisticCacheFacade;
import com.ckb.explorer.util.I18n;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticsController {

    @Resource
    private IStatisticCacheFacade statisticCacheFacade;

  @Resource
  private I18n i18n;

    /**
     * 获取统计信息索引
     */
    @GetMapping
    @Operation(summary = "获取统计信息索引")
    public ResponseInfo<IndexStatisticResponse> index() {
        // 设置缓存控制头
        // 注意：在实际项目中，可能需要根据Spring版本和配置调整缓存策略
        return ResponseInfo.SUCCESS(statisticCacheFacade.getIndexStatistic());
    }

    /**
     * 获取指定统计信息
     */
    @GetMapping("/{fieldName}")
    @Operation(summary = "获取指定统计信息")
    public ResponseInfo<StatisticResponse> show(@PathVariable String fieldName) {
        // 验证查询参数
        validateQueryParams(fieldName);

        return ResponseInfo.SUCCESS(statisticCacheFacade.getStatisticByFieldName(fieldName));
    }

    /**
     * 验证查询参数
     */
    private void validateQueryParams(String fieldName) {
        // 检查查询参数是否为空或不在有效的统计信息名称列表中
        if (StringUtils.isEmpty(fieldName) || fieldName.trim().isEmpty() || !getValidStatisticInfoNames().contains(fieldName)) {
            throw new ServerException(I18nKey.STATISTIC_INFO_NAME_INVALID_CODE, i18n.getMessage(I18nKey.STATISTIC_INFO_NAME_INVALID_MESSAGE));
        }
    }
    
    /**
     * 获取有效的统计信息名称列表
     */
    private Set<String> getValidStatisticInfoNames() {
//        List<String> validNames = Arrays.stream(StatisticResponse.class.getDeclaredFields())
//          .map(Field::getName)                    // 获取字段名
//          .map(FieldNameConverter::camelToSnake)  // 转为 snake_case
//          .collect(Collectors.toList());
//        return validNames;
      Set<String> validNames = new HashSet<>();
      validNames.add("tip_block_number");
      validNames.add("blockchain_info");
      validNames.add("flush_cache_info");
      validNames.add("address_balance_ranking");

      return validNames;
    }
}