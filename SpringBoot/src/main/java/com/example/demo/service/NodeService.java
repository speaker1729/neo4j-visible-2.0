package com.example.demo.service;

import com.example.demo.nodes.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeService {
    @Autowired
    private ReasonRepository reasonRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private POIRepository poiRepository;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private TimeRepository timeRepository;
    @Autowired
    private WeatherRepository weatherRepository;
    @Autowired
    private TripModeRepository TripModeRepository;

    public List<LocationEntity> findAllLocation() {
        return locationRepository.findAllLocation();
    }

    public List<AreaEntity> findAllArea() {
        return areaRepository.findAllArea();
    }

    public List<CityEntity> findAllCity() {
        return cityRepository.findAllCity();
    }

    public List<ProvinceEntity> findAllProvince() {
        return provinceRepository.findAllProvince();
    }

    public List<WeatherEntity> findAllWeather() {
        return weatherRepository.findAllWeather();
    }

    public List<TimeEntity> findAllTime() {
        return timeRepository.findAllTime();
    }

    public List<PersonEntity> findAllPerson() {
        return personRepository.findAllPerson();
    }

    public List<ReasonEntity> findAllReason() {
        return reasonRepository.findAllReason();
    }

    public List<POIEntity> findAllPOI() {
        return poiRepository.findAllPOI();
    }

    public List<TripModeEntity> findAllTripMode() {
        return TripModeRepository.findAllTripMode();
    }

    public List<ProvinceEntity> findByProvinceName(String ProvinceName) {
        return provinceRepository.findByProvinceName(ProvinceName);
    }

    public List<CityEntity> findByCityName(String CityName) {
        return cityRepository.findByCityName(CityName);
    }

    public List<AreaEntity> findByAreaName(String AreaName) {
        return areaRepository.findByAreaName(AreaName);
    }

    public List<LocationEntity> findByLocationName(String PlaceName) {
        return locationRepository.findByLocationName(PlaceName);
    }

    public List<TimeEntity> findByTimeName(String TimeName) {
        return timeRepository.findByTimeName(TimeName);
    }

    public List<WeatherEntity> findByWeatherName(String WeatherName) {
        return weatherRepository.findByWeatherName(WeatherName);
    }

    public List<PersonEntity> findByPersonName(String PersonName) {
        return personRepository.findByPersonName(PersonName);
    }

    public List<POIEntity> findByPOIName(String POIName) {
        return poiRepository.findByPOIName(POIName);
    }

    public List<ReasonEntity> findByReasonName(String ReasonName) {
        return reasonRepository.findByReasonName(ReasonName);
    }

    public List<TripModeEntity> findByTripModeName(String TripModeName) {
        return TripModeRepository.findByTripModeName(TripModeName);
    }

    public List<LocationEntity> findLocationByProvince(String provinceName) {
        return locationRepository.findLocationByProvince(provinceName);
    }

    public List<LocationEntity> findLocationByCity(String cityName) {
        return locationRepository.findLocationByCity(cityName);
    }

    public List<LocationEntity> findLocationByArea(String areaName) {
        return locationRepository.findLocationByArea(areaName);
    }
}