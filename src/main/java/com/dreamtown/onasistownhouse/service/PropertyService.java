package com.dreamtown.onasistownhouse.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Example;
import com.dreamtown.onasistownhouse.entity.MWilayah;
import com.dreamtown.onasistownhouse.entity.Property;
import com.dreamtown.onasistownhouse.entity.PropertyDetails;
import com.dreamtown.onasistownhouse.entity.PropertyStatus;
import com.dreamtown.onasistownhouse.repository.PropertyDetailsRepository;
import com.dreamtown.onasistownhouse.repository.PropertyRepository;

@Service
public class PropertyService {
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private PropertyDetailsRepository propertyDetailsRepository;

    public Page<Property> findPaginated(Pageable pageable, Integer idWilayah) {
        Property p = new Property();
        p.setWilayah(new MWilayah(idWilayah, null));
        Example<Property> exampleProperty = Example.of(p);
        List<Property> listProperty = propertyRepository.findAll(exampleProperty);
        int s = (int) pageable.getOffset();
        int e = Math.min((s + pageable.getPageSize()), listProperty.size());
        return new PageImpl<>(listProperty.subList(s, e), pageable, listProperty.size());
    }

    public Page<Property> listRekomendasi(Pageable pageable, Boolean isAdmin) {
        List<Property> tempListProperty = propertyRepository.findAll();
        List<Property> listProperty = new ArrayList<>();
        for (Property p : tempListProperty) {
            p.getListPropertyDetails().clear();
            List<PropertyDetails> propertyDetails = new ArrayList<>();
            Optional<PropertyDetails> tempDetails = propertyDetailsRepository
                    .findFirstByIdPropertyOrderByHargaAsc(p.getIdProperty());
            if (tempDetails.isPresent()) {
                PropertyDetails pDetails = tempDetails.get();
                if (isAdmin) {
                    propertyDetails.add(tempDetails.get());
                    p.setListPropertyDetails(propertyDetails);
                    listProperty.add(p);
                } else {
                    if (pDetails.getPropertyStatus().getIdPropertyStatus() != 2) {
                        propertyDetails.add(tempDetails.get());
                        p.setListPropertyDetails(propertyDetails);
                        listProperty.add(p);
                    }
                }
            }

        }
        int s = (int) pageable.getOffset();
        int e = Math.min((s + pageable.getPageSize()), listProperty.size());
        return new PageImpl<>(listProperty.subList(s, e), pageable, listProperty.size());
    }

    public Property getPropertyByName(String propertyName) {
        return propertyRepository.findOneByPropertyName(propertyName);
    }
}
