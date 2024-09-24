package com.example.demo.service;

import com.example.demo.RelationDTO.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationService {

    @Autowired
    private LocatedInRepository locatedInRepository;
    @Autowired
    private LostWeatherRepository lostWeatherRepository;
    @Autowired
    private LostTimeRepository lostTimeRepository;
    @Autowired
    private LostPOIRepository lostPOIRepository;
    @Autowired
    private LostReasonRepository lostReasonRepository;
    @Autowired
    private LostPersonRepository lostPersonRepository;
    @Autowired
    private ByRepository byRepository;

    public List<LocatedInDTO> findLocatedIn() {
        List<LocatedInDTO> result;
        result = locatedInRepository.findLocatedIn();
        return result;
    }

    public List<LostWeatherDTO> findLostWeather() {
        List<LostWeatherDTO> result;
        result = lostWeatherRepository.findLostWeather();
        return result;
    }

    public List<LostTimeDTO> findLostTime() {
        List<LostTimeDTO> result;
        result = lostTimeRepository.findLostTime();
        return result;
    }

    public List<LostReasonDTO> findLostReason() {
        List<LostReasonDTO> result;
        result = lostReasonRepository.findLostReason();
        return result;
    }

    public List<LostPOIDTO> findLostPOI() {
        List<LostPOIDTO> result;
        result = lostPOIRepository.findLostPOI();
        return result;
    }

    public List<LostPersonDTO> findLostPerson() {
        List<LostPersonDTO> result;
        result = lostPersonRepository.findLostPerson();
        return result;
    }

    public List<ByDTO> findBy() {
        List<ByDTO> result;
        result = byRepository.findBY();
        return result;
    }
}
