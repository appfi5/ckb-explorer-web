package com.ckb.explorer.service;

public interface SuggestQueryService {

    /**
     * 根据查询关键字和过滤条件查找建议结果
     * @param queryKey 查询关键字
     * @param filterBy 过滤条件
     * @return 查询结果
     */
    Object find(String queryKey, String filterBy);
}