package com.ckb.explorer.exceptions;

/**
 * 业务异常基类（对应 Ruby 的 Api::V1::Exceptions::Error）
 */
public class ApiError extends RuntimeException {
    // 错误码（如 1007）
    private final int code;
    // HTTP 状态码（如 400）
    private final int status;
    // 错误标题（如 "Page Param Invalid"）
    private final String title;
    // 错误详情（如 "Params page should be an integer"）
    private final String detail;
    // 帮助文档链接
    private final String href;

    // 构造器：供子类调用，初始化所有属性
    protected ApiError(int code, int status, String title, String detail, String href) {
        super(title); // 调用父类 Exception 的 message 字段（存储 title）
        this.code = code;
        this.status = status;
        this.title = title;
        this.detail = detail;
        this.href = href;
    }

    // Getter 方法：提供属性访问（Java 无 attr_accessor，需显式定义）
    public int getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public String getHref() {
        return href;
    }
}