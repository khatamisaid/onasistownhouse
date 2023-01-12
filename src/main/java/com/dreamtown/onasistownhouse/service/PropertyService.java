package com.dreamtown.onasistownhouse.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Example;
import com.dreamtown.onasistownhouse.entity.MWilayah;
import com.dreamtown.onasistownhouse.entity.Property;
import com.dreamtown.onasistownhouse.repository.PropertyRepository;

@Service
public class PropertyService {
    @Autowired
    private PropertyRepository propertyRepository;

    public Page<Property> findPaginated(Pageable pageable, Integer idWilayah){
        Property p = new Property();
        p.setWilayah(new MWilayah(idWilayah, null));
        Example<Property> exampleProperty = Example.of(p);
        List<Property> listProperty = propertyRepository.findAll(exampleProperty);
        int s = (int) pageable.getOffset();
        int e = Math.min((s + pageable.getPageSize()), listProperty.size());
        return new PageImpl<>(listProperty.subList(s, e), pageable, listProperty.size());
    }

    public Property getPropertyByName(String propertyName){
        return propertyRepository.findOneByPropertyName(propertyName);
    }
}
