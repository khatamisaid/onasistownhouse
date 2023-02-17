package com.dreamtown.onasistownhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dreamtown.onasistownhouse.entity.LogAktivitas;

public interface LogAktivitasRepository extends JpaRepository<LogAktivitas, Long>{
    @Query(value = "select count(*) as jumlah from log_aktivitas where created_at between STR_TO_DATE(?1,'%Y%m%d') and STR_TO_DATE(?2,'%Y%m%d')", nativeQuery = true)
    Long logHariBetween(String p1,String p2);
}
