package com.dreamtown.onasistownhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dreamtown.onasistownhouse.entity.PropertyDetails;

public interface PropertyDetailsRepository extends JpaRepository<PropertyDetails, Integer> {
    @Modifying
    @Query("delete from PropertyDetails pd where pd.idProperty=:idProperty")
    void deleteWhereIdProperty(@Param("idProperty") Integer idProperty);
    
    PropertyDetails findOneByIdDetailsPropertyAndTipeProperty(Integer idDetailsProperty, String tipeProperty); 
}
