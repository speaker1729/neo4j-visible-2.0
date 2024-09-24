package com.example.demo.controller;

import com.example.demo.nodes.*;
import com.example.demo.service.NodeService;
import com.example.demo.util.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Node")
public class NodeController {
    @Autowired
    private NodeService nodeService;

    @PostMapping("/findAllLocation")
    public RespBean findAllLocation() {
        return RespBean.success("查询成功", nodeService.findAllLocation());
    }

    @PostMapping("/findAllArea")
    public RespBean findAllArea() {
        return RespBean.success("查询成功", nodeService.findAllArea());
    }

    @PostMapping("/findAllCity")
    public RespBean findAllCity() {
        return RespBean.success("查询成功", nodeService.findAllCity());
    }

    @PostMapping("/findAllProvince")
    public RespBean findAllProvince() {
        return RespBean.success("查询成功", nodeService.findAllProvince());
    }

    @PostMapping("/findAllWeather")
    public RespBean findAllWeather() {
        return RespBean.success("查询成功", nodeService.findAllWeather());
    }

    @PostMapping("/findAllTime")
    public RespBean findAllTime() {
        return RespBean.success("查询成功", nodeService.findAllTime());
    }

    @PostMapping("/findAllReason")
    public RespBean findAllReason() {
        return RespBean.success("查询成功", nodeService.findAllReason());
    }

    @PostMapping("/findAllPOI")
    public RespBean findAllPOI() {
        return RespBean.success("查询成功", nodeService.findAllPOI());
    }

    @PostMapping("/findAllPerson")
    public RespBean findAllPerson() {
        return RespBean.success("查询成功", nodeService.findAllPerson());
    }

    @PostMapping("/findAllTripMode")
    public RespBean findAllTripMode() {
        return RespBean.success("查询成功", nodeService.findAllTripMode());
    }

    @PostMapping("/findByProvinceName")
    public RespBean findByProvinceName(@RequestParam("ProvinceName") String ProvinceName) {
        if (ProvinceName == null) {
            return RespBean.error("ProvinceName 不能为空", null);
        }
        List<ProvinceEntity> result = nodeService.findByProvinceName(ProvinceName);
        if (result.isEmpty()) {
            return RespBean.error("未找到匹配的区域", null);
        }
        return RespBean.success("成功查询", result);
    }

    @PostMapping("/findByCityName")
    public RespBean findByCityName(@RequestParam("CityName") String CityName) {
        if (CityName == null) {
            return RespBean.error("CityName 不能为空", null);
        }
        List<CityEntity> result = nodeService.findByCityName(CityName);
        if (result.isEmpty()) {
            return RespBean.error("未找到匹配的区域", null);
        }
        return RespBean.success("成功查询", result);
    }

    @PostMapping("/findByAreaName")
    public RespBean findByAreaName(@RequestParam("AreaName") String AreaName) {
        if (AreaName == null) {
            return RespBean.error("AreaName 不能为空", null);
        }
        List<AreaEntity> result = nodeService.findByAreaName(AreaName);
        if (result.isEmpty()) {
            return RespBean.error("未找到匹配的区域", null);
        }
        return RespBean.success("成功查询", result);
    }

    @PostMapping("/findByLocationName")
    public RespBean findByLocationName(@RequestParam("LocationName") String LocationName) {
        if (LocationName == null) {
            return RespBean.error("LocationName 不能为空", null);
        }
        List<LocationEntity> result = nodeService.findByLocationName(LocationName);
        if (result.isEmpty()) {
            return RespBean.error("未找到匹配的区域", null);
        }
        return RespBean.success("成功查询", result);
    }

    @PostMapping("/findByTimeName")
    public RespBean findByTimeName(@RequestParam("TimeName") String TimeName) {
        if (TimeName == null) {
            return RespBean.error("TimeName 不能为空", null);
        }
        List<TimeEntity> result = nodeService.findByTimeName(TimeName);
        if (result.isEmpty()) {
            return RespBean.error("未找到匹配的区域", null);
        }
        return RespBean.success("成功查询", result);
    }

    @PostMapping("/findByWeatherName")
    public RespBean findByWeatherName(@RequestParam("WeatherName") String WeatherName) {
        if (WeatherName == null) {
            return RespBean.error("WeatherName 不能为空", null);
        }
        List<WeatherEntity> result = nodeService.findByWeatherName(WeatherName);
        if (result.isEmpty()) {
            return RespBean.error("未找到匹配的区域", null);
        }
        return RespBean.success("成功查询", result);
    }

    @PostMapping("/findByPersonName")
    public RespBean findByPersonName(@RequestParam("PersonName") String PersonName) {
        if (PersonName == null) {
            return RespBean.error("PersonName 不能为空", null);
        }
        List<PersonEntity> result = nodeService.findByPersonName(PersonName);
        if (result.isEmpty()) {
            return RespBean.error("未找到匹配的区域", null);
        }
        return RespBean.success("成功查询", result);
    }

    @PostMapping("/findByPOIName")
    public RespBean findByPOIName(@RequestParam("POIName") String POIName) {
        if (POIName == null) {
            return RespBean.error("POIName 不能为空", null);
        }
        List<POIEntity> result = nodeService.findByPOIName(POIName);
        if (result.isEmpty()) {
            return RespBean.error("未找到匹配的区域", null);
        }
        return RespBean.success("成功查询", result);
    }

    @PostMapping("/findByReasonName")
    public RespBean findByReasonName(@RequestParam("ReasonName") String ReasonName) {
        if (ReasonName == null) {
            return RespBean.error("ReasonName 不能为空", null);
        }
        List<ReasonEntity> result = nodeService.findByReasonName(ReasonName);
        if (result.isEmpty()) {
            return RespBean.error("未找到匹配的区域", null);
        }
        return RespBean.success("成功查询", result);
    }

    @PostMapping("/findByTripModeName")
    public RespBean findByTripModeName(@RequestParam("TripModeName") String TripModeName) {
        if (TripModeName == null) {
            return RespBean.error("TripModeName 不能为空", null);
        }
        List<TripModeEntity> result = nodeService.findByTripModeName(TripModeName);
        if (result.isEmpty()) {
            return RespBean.error("未找到匹配的区域", null);
        }
        return RespBean.success("成功查询", result);
    }

    @PostMapping("/findLocationByProvince")
    public RespBean getLocationByProvince(@RequestParam("provinceName") String provinceName) {
        if (provinceName == null || provinceName.trim().isEmpty()) {
            return RespBean.error("provinceName 不能为空", null);
        }
        List<LocationEntity> result = nodeService.findLocationByProvince(provinceName);
        if (result.isEmpty()) {
            return RespBean.error("未找到匹配的省份", null);
        }
        return RespBean.success("成功查询", result);
    }

    @PostMapping("/findLocationByCity")
    public RespBean getLocationByCity(@RequestParam("cityName") String cityName) {
        if (cityName == null || cityName.trim().isEmpty()) {
            return RespBean.error("cityName 不能为空", null);
        }
        List<LocationEntity> result = nodeService.findLocationByCity(cityName);
        if (result.isEmpty()) {
            return RespBean.error("未找到匹配的城市", null);
        }
        return RespBean.success("成功查询", result);
    }

    @PostMapping("/findLocationByArea")
    public RespBean getLocationByArea(@RequestParam("areaName") String areaName) {
        if (areaName == null || areaName.trim().isEmpty()) {
            return RespBean.error("areaName 不能为空", null);
        }
        List<LocationEntity> result = nodeService.findLocationByArea(areaName);
        if (result.isEmpty()) {
            return RespBean.error("未找到匹配的地区", null);
        }
        return RespBean.success("成功查询", result);
    }
}
