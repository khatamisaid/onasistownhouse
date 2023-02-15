package com.dreamtown.onasistownhouse.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dreamtown.onasistownhouse.entity.LogWhatsApp;

public interface LogWhatsAppRepository extends JpaRepository<LogWhatsApp, Long>{
    Optional<LogWhatsApp> findOneByNamaContact(String namaContact);
}