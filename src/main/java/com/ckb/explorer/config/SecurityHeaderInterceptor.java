package com.ckb.explorer.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 安全响应头拦截器：统一添加XSS、CSRF、点击劫持等防护头
 */
public class SecurityHeaderInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, org.springframework.web.servlet.ModelAndView modelAndView) {
        // 1. 防止MIME类型嗅探
        response.setHeader("X-Content-Type-Options", "nosniff");
        // 2. 禁止页面被嵌入iframe（防止点击劫持）
        response.setHeader("X-Frame-Options", "DENY");
        // 3. 启用XSS保护，检测到XSS攻击时阻止页面加载
        response.setHeader("X-XSS-Protection", "1; mode=block");
        // 4. 内容安全策略（CSP）- 按需调整，下面是基础配置示例
        response.setHeader("Content-Security-Policy",
                "default-src 'self'; " +  // 默认只允许加载本站资源
                        "script-src 'self' 'unsafe-inline'; " +  // 脚本只允许本站+内联（可根据实际调整）
                        "style-src 'self' 'unsafe-inline'; " +   // 样式只允许本站+内联
                        "img-src 'self' data:; " +               // 图片允许本站+base64数据
                        "frame-ancestors 'none';");              // 禁止任何iframe嵌入
        // 5. 严格传输安全（HSTS）- 强制HTTPS，有效期1年（仅HTTPS环境生效）
        response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
    }
}