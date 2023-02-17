package com.dreamtown.onasistownhouse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dreamtown.onasistownhouse.entity.LogWhatsApp;

public interface LogWhatsAppRepository extends JpaRepository<LogWhatsApp, Long>{
    Optional<LogWhatsApp> findOneByNamaContact(String namaContact);
    
    @Query(value = "select count(*) as jumlah, nama_contact from log_whatsapp where created_at between STR_TO_DATE(?1,'%Y%m%d') and STR_TO_DATE(?2,'%Y%m%d') group by nama_contact", nativeQuery = true)
    List<Object[]> logWhatsApp(String p1,String p2);
}