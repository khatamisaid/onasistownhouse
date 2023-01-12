package com.dreamtown.onasistownhouse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dreamtown.onasistownhouse.entity.PropertyDetails;
import com.dreamtown.onasistownhouse.repository.PropertyDetailsRepository;

@Service
public class PropertyDetailsService {
    @Autowired
    private PropertyDetailsRepository propertyDetailsRepository;

    public PropertyDetails getPropertyDetails(Integer id, Integer sortBy){
        if(sortBy == 1){
            return propertyDetailsRepository.findDetailsPropertyByIdOrderByHargaAsc(id).get(0);
        }else if(sortBy == 2){
            return propertyDetailsRepository.findDetailsPropertyByIdOrderByHargaDesc(id).get(0);
        }
        return new PropertyDetails();
    }
}
