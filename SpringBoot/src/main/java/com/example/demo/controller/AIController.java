package com.example.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


// 在你的后端控制器中
@RestController
public class AIController {

    @GetMapping("/proxyBaiduToken")
    public ResponseEntity<String> proxyBaiduToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=gGvHGScrNXcYBQT7WpMK7PL0&client_secret=Pm0h810KZGfRKcvTxU7BE7Q6dwO7TQAG";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return ResponseEntity.ok(response.getBody());
    }

    // 转发对百度 ERNIE-Bot API 的请求
    @PostMapping("/callBaiduAI")
    public ResponseEntity<String> callBaiduAI(@RequestBody Map<String, Object> requestData, @RequestHeader("access_token") String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 设置 access_token 为请求头参数
        headers.set("access_token", accessToken);
        // 指定请求的 URL
        String url = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions?access_token=" + accessToken;
        // 将 requestData 转换为 JSON 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String requestDataJson;
        try {
            requestDataJson = objectMapper.writeValueAsString(requestData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting request data to JSON.");
        }
        // 发送转发请求，包括 URL、Headers 和 Data
        HttpEntity<String> entity = new HttpEntity<>(requestDataJson, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return ResponseEntity.ok(response.getBody());
    }
}
