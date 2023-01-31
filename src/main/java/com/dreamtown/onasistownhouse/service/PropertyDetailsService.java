package com.dreamtown.onasistownhouse.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dreamtown.onasistownhouse.entity.PropertyDetails;
import com.dreamtown.onasistownhouse.entity.PropertyStatus;
import com.dreamtown.onasistownhouse.repository.PropertyDetailsRepository;

@Service
public class PropertyDetailsService {
    @Autowired
    private PropertyDetailsRepository propertyDetailsRepository;

    public PropertyDetails getPropertyDetails(Integer id, String tipeProperty) {
        return propertyDetailsRepository.findFirstByIdPropertyAndTipeProperty(id, tipeProperty);
    }

    public List<PropertyDetails> findAllByIdPropertyAndPropertyStatusIsNotOrderByTipePropertyAsc(Integer idProperty) {
        return propertyDetailsRepository.findAllByIdPropertyOrderByTipePropertyAsc(idProperty);
    }
}
