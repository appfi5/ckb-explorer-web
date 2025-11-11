package com.ckb.explorer.util;


import jakarta.annotation.Resource;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class I18n {
  @Resource
  private MessageSource messageSource;

  public String getMessage(String key,Object... params){
    return messageSource.getMessage(key, params,key, LocaleContextHolder.getLocale());
  }
}
