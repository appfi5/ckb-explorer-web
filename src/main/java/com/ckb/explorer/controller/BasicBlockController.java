package com.ckb.explorer.controller;

import com.ckb.explorer.entity.BasicBlock;
import com.ckb.explorer.service.BasicBlockService;
import com.ckb.explorer.domain.resp.BasicBlockResponse;
import com.ckb.explorer.mapstruct.BasicBlockConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/v1/basic-blocks")
public class BasicBlockController {

    private final BasicBlockService basicBlockService;
    private final BasicBlockConvert basicBlockConvert;

    @Autowired
    public BasicBlockController(BasicBlockService basicBlockService, BasicBlockConvert basicBlockConvert) {
        this.basicBlockService = basicBlockService;
        this.basicBlockConvert = basicBlockConvert;
    }

    @GetMapping
    @Operation(summary = "获取全部块")
    public ResponseEntity<List<BasicBlockResponse>> getAllBasicBlocks() {
        List<BasicBlock> basicBlocks = basicBlockService.getAllBasicBlocks();
        List<BasicBlockResponse> responses = basicBlockConvert.convertList(basicBlocks);
        return ResponseEntity.ok(responses);
    }
}