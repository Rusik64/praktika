package com.example.prak.service;

import com.example.prak.repository.CityRepository;
import com.example.prak.repository.model.City;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {
    private CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<City> getCities() {
        return cityRepository.findAll(Sort.by(Sort.Order.asc("name")));
    }

    public City getCity(Long cityId) {
        return cityRepository.findById(cityId).orElse(null);
    }
}
