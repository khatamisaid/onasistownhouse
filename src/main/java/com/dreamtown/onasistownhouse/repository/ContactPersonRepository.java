package com.dreamtown.onasistownhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dreamtown.onasistownhouse.entity.ContactPerson;

public interface ContactPersonRepository extends JpaRepository<ContactPerson, Integer>{
    
}
