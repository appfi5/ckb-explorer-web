package com.ckb.explorer.controller;

import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.config.ServerException;
import com.ckb.explorer.constants.I18nKey;
import com.ckb.explorer.domain.resp.MonetaryDataResponse;
import com.ckb.explorer.facade.IMonetaryDataCacheFacade;
import com.ckb.explorer.util.I18n;
import com.ckb.explorer.util.MonetaryData;
import jakarta.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/monetary_data")
public class MonetaryDataController {

  @Resource
  private IMonetaryDataCacheFacade monetaryDataCacheFacade;

  @Resource
  private I18n i18n;

  /**
   * 对应Ruby的show动作：处理指标查询，返回动态序列化结果
   */
  @GetMapping("/{indicator}")
  public ResponseInfo<MonetaryDataResponse> show(@PathVariable("indicator") String indicator) {

    // 验证查询参数
    validateQueryParams(indicator.trim());


    // 调用缓存门面获取数据
    MonetaryDataResponse data = monetaryDataCacheFacade.getMonetaryData(indicator.trim());

    return ResponseInfo.SUCCESS(data);
  }

  /**
   * 验证查询参数
   *
   * @param indicator 指标名称
   * @throws IllegalArgumentException 当参数无效时抛出异常
   */
  private void validateQueryParams(String indicator) {
    // 验证指标名称不能为空
    if (indicator == null || indicator.isEmpty()) {
      throw new IllegalArgumentException("Indicator name cannot be empty");
    }

    // query_keys = query_key.split("-")
    List<String> queryKeys = Arrays.stream(indicator.split("-"))
        .map(String::trim) // 去除每个key的前后空格（避免"nominal_apc 50"这类无效格式）
        .filter(key -> !key.isEmpty()) // 过滤分割后的空字符串（如queryKey以"-"开头/结尾）
        .toList();

    // 3. 计算extra_keys：分割后的key中，不在VALID_INDICATORS里的元素（对应Ruby的query_keys - ::MonetaryData::VALID_INDICATORS）
    List<String> extraKeys = queryKeys.stream()
        .filter(key -> !MonetaryData.VALID_INDICATORS.contains(key))
        .toList();

    // 4. 验证逻辑：extraKeys为空 OR (extraKeys大小为1 且 唯一元素匹配nominal_apc+数字的正则)
    if (extraKeys.size() == 1) {
      String onlyExtraKey = extraKeys.getFirst();
      if(!onlyExtraKey.matches(MonetaryData.NOMINAL_APC_REGEX)){
        throw new ServerException(I18nKey.INDICATOR_NAME_INVALID_CODE,
            i18n.getMessage(I18nKey.INDICATOR_NAME_INVALID_MESSAGE));
      }
    } else if (extraKeys.size() > 1){
      throw new ServerException(I18nKey.INDICATOR_NAME_INVALID_CODE,
          i18n.getMessage(I18nKey.INDICATOR_NAME_INVALID_MESSAGE));
    }
  }
}
