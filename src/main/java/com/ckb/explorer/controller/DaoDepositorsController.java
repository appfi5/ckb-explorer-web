package com.ckb.explorer.controller;

import com.ckb.explorer.common.dto.ResponseInfo;
import com.ckb.explorer.domain.resp.DaoDepositorResponse;
import com.ckb.explorer.facade.IDaoDepositorCacheFacade;
import com.ckb.explorer.service.DaoDepositorService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * DAO存款者控制器
 * 对应Ruby代码中的Api::V1::DaoDepositorsController
 */
@RestController
@RequestMapping("/api/v1/dao_depositors")
public class DaoDepositorsController {

    @Resource
    private IDaoDepositorCacheFacade daoDepositorCacheFacade;

    /**
     * 获取DAO存款者列表
     * 对应Ruby代码中的index方法
     * @return DAO存款者列表响应
     */
    @GetMapping
    public ResponseInfo<List<DaoDepositorResponse>> index() {
        // 调用服务层获取前100名DAO存款者列表
        List<DaoDepositorResponse> depositors = daoDepositorCacheFacade.getTopDaoDepositors();
        return ResponseInfo.SUCCESS(depositors);
    }

    /**
     * 下载DAO存款者CSV文件
     * 对应Ruby代码中的download_csv方法
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param startNumber 开始编号
     * @param endNumber 结束编号
     * @return CSV文件响应
     */
    @GetMapping("/download_csv")
    public ResponseEntity<byte[]> downloadCsv(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Integer startNumber,
            @RequestParam(required = false) Integer endNumber) {
        // 调用服务层生成CSV文件
        byte[] csvContent = daoDepositorService.generateDaoDepositorsCsv(startDate, endDate, startNumber, endNumber);
        
        // 设置响应头，指定文件下载
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv; charset=utf-8; header=present"));
        headers.setContentDispositionFormData("attachment", "dao_depositors.csv");
        headers.setContentLength(csvContent.length);
        
        // 返回CSV文件响应
        return ResponseEntity.ok()
                .headers(headers)
                .body(csvContent);
    }
}
