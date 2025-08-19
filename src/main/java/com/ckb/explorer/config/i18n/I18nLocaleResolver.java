package com.ckb.explorer.config.i18n;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Locale;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

/**
 * I18nLocaleResolver
 * get language from:
 *  1.[http request header] [2.request parameter]
 * the parameter key is: lang
 * supported language is: en_US, zh_CN
 */
public class I18nLocaleResolver implements LocaleResolver {

    private static final String LANG_PARAMETER = "accept-language";

    private static final String PATH_PARAMETER_SPLIT = "_";

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String lang = request.getHeader(LANG_PARAMETER);
        if(StringUtils.isEmpty(lang)){
            lang = request.getParameter(LANG_PARAMETER);
        }
        Locale locale = null;
        if (lang.contains("zh_CN")|| lang.contains("en_US")) {
            String[] split = lang.split(PATH_PARAMETER_SPLIT);
            locale = new Locale(split[0], split[1].substring(0,1));
        }else{
            locale = request.getLocale();
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        throw new UnsupportedOperationException("Cannot save - use a different locale resolution strategy");
    }
}
