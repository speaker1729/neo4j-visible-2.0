package com.example.demo.controller;

import com.example.demo.RelationDTO.*;
import com.example.demo.service.RelationService;
import com.example.demo.util.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Relation")
public class RelationController {

    @Autowired
    private RelationService relationService;

    @PostMapping("/find_located_in")
    public RespBean getLocatedIn() {
        try {
            List<LocatedInDTO> result = relationService.findLocatedIn();
            return RespBean.success("成功查询", result);
        } catch (Exception e) {
            return RespBean.error("查询失败: " + e.getMessage(), null);
        }
    }

    @PostMapping("/find_lost_weather")
    public RespBean findLostWeather() {
        try {
            List<LostWeatherDTO> result = relationService.findLostWeather();
            return RespBean.success("成功查询", result);
        } catch (Exception e) {
            return RespBean.error("查询失败: " + e.getMessage(), null);
        }
    }

    @PostMapping("/find_lost_reason")
    public RespBean findLostReason() {
        try {
            List<LostReasonDTO> result = relationService.findLostReason();
            return RespBean.success("成功查询", result);
        } catch (Exception e) {
            return RespBean.error("查询失败: " + e.getMessage(), null);
        }
    }

    @PostMapping("/find_lost_person")
    public RespBean findLostPerson() {
        try {
            List<LostPersonDTO> result = relationService.findLostPerson();
            return RespBean.success("成功查询", result);
        } catch (Exception e) {
            return RespBean.error("查询失败: " + e.getMessage(), null);
        }
    }

    @PostMapping("/find_lost_time")
    public RespBean findLostTime() {
        try {
            List<LostTimeDTO> result = relationService.findLostTime();
            return RespBean.success("成功查询", result);
        } catch (Exception e) {
            return RespBean.error("查询失败: " + e.getMessage(), null);
        }
    }

    @PostMapping("/find_lost_POI")
    public RespBean findLostPOI() {
        try {
            List<LostPOIDTO> result = relationService.findLostPOI();
            return RespBean.success("成功查询", result);
        } catch (Exception e) {
            return RespBean.error("查询失败: " + e.getMessage(), null);
        }
    }

    @PostMapping("/find_by")
    public RespBean findBy() {
        try {
            List<ByDTO> result = relationService.findBy();
            return RespBean.success("成功查询", result);
        } catch (Exception e) {
            return RespBean.error("查询失败: " + e.getMessage(), null);
        }
    }
}
