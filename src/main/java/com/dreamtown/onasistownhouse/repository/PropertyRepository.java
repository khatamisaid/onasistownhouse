package com.dreamtown.onasistownhouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dreamtown.onasistownhouse.entity.MWilayah;
import com.dreamtown.onasistownhouse.entity.Property;

public interface PropertyRepository extends JpaRepository<Property, Integer> {

    Property findOneByPropertyName(String propertyName);

    List<Property> findByWilayah(MWilayah wilayah);
}
