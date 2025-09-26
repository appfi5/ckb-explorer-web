package com.ckb.explorer.controller;

import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.service.SuggestQueryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/suggest_queries")
public class SuggestQueriesController {

    @Resource
    private SuggestQueryService suggestQueryService;

    @GetMapping
    public ResponseInfo<?> index(@RequestParam String q, @RequestParam(required = false) Integer filterBy) {

        Object response = suggestQueryService.find(q, filterBy);
        return ResponseInfo.SUCCESS(response);
    }
}