package com.dreamtown.onasistownhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dreamtown.onasistownhouse.entity.Property;

public interface PropertyRepository extends JpaRepository<Property, Integer> {

}
