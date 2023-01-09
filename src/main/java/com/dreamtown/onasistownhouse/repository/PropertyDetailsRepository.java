package com.dreamtown.onasistownhouse.repository;

import java.util.List;

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



    @Query(value = "SELECT DISTINCT tipe_property from property_details where id_details_property = ?1 order by tipe_property ASC", nativeQuery = true)
    List<String> findTipePropertyByIdDetailsProperty(Integer idDetailsProperty);
}
