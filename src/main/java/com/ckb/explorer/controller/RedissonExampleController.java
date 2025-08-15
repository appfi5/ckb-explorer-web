package com.ckb.explorer.controller;

import com.ckb.explorer.service.RedissonExampleService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v2/redisson")
public class RedissonExampleController {

    private final RedissonExampleService redissonExampleService;

    @Autowired
    public RedissonExampleController(RedissonExampleService redissonExampleService) {
        this.redissonExampleService = redissonExampleService;
    }

    @Operation(summary = "设置缓存值")
    @PostMapping("/set")
    public ResponseEntity<String> setValue(
            @RequestParam String key,
            @RequestParam String value,
            @RequestParam(defaultValue = "60") long timeout) {

        redissonExampleService.setValue(key, value, timeout);
        return ResponseEntity.ok("值已设置: " + key + " = " + value);
    }

    @Operation(summary = "获取缓存值")
    @GetMapping("/get/{key}")
    public ResponseEntity<Object> getValue(@PathVariable String key) {
        Object value = redissonExampleService.getValue(key);
        if (value != null) {
            return ResponseEntity.ok(value);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "删除缓存值")
    @DeleteMapping("/delete/{key}")
    public ResponseEntity<String> deleteValue(@PathVariable String key) {
        boolean deleted = redissonExampleService.deleteValue(key);
        if (deleted) {
            return ResponseEntity.ok("值已删除: " + key);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "尝试获取锁")
    @PostMapping("/lock")
    public ResponseEntity<String> tryLock(
            @RequestParam String lockKey,
            @RequestParam(defaultValue = "10") long waitTime,
            @RequestParam(defaultValue = "30") long leaseTime,
            @RequestParam(defaultValue = "SECONDS") TimeUnit unit) {

        boolean locked = redissonExampleService.tryLock(lockKey, waitTime, leaseTime, unit);
        if (locked) {
            return ResponseEntity.ok("成功获取锁: " + lockKey);
        } else {
            return ResponseEntity.status(408).body("获取锁超时: " + lockKey);
        }
    }

    @Operation(summary = "释放锁")
    @PostMapping("/unlock")
    public ResponseEntity<String> unlock(@RequestParam String lockKey) {
        redissonExampleService.unlock(lockKey);
        return ResponseEntity.ok("已释放锁: " + lockKey);
    }
}