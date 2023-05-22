package com.dreamtown.onasistownhouse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dreamtown.onasistownhouse.entity.PropertyDetails;

public interface PropertyDetailsRepository extends JpaRepository<PropertyDetails, Integer> {

    @Modifying
    @Query("delete from PropertyDetails pd where pd.idProperty=:idProperty")
    void deleteWhereIdProperty(@Param("idProperty") Integer idProperty);

    PropertyDetails findOneByIdPropertyAndTipeProperty(Integer idDetailsProperty, String tipeProperty);

    @Query(value = "SELECT * from property_details where id_property = ?1 order by harga ASC", nativeQuery = true)
    List<PropertyDetails> findDetailsPropertyByIdOrderByHargaAsc(Integer id);

    @Query(value = "SELECT * from property_details where id_property = ?1 order by harga DESC", nativeQuery = true)
    List<PropertyDetails> findDetailsPropertyByIdOrderByHargaDesc(Integer id);

    @Query(value = "SELECT DISTINCT tipe_property from property_details where id_property = ?1 order by tipe_property ASC", nativeQuery = true)
    List<String> findDistinctTipePropertyByIdProperty(Integer idProperty);

    Optional<PropertyDetails> findFirstByIdPropertyOrderByHargaAsc(Integer idProperty);

    Optional<PropertyDetails> findFirstByIdPropertyOrderByHargaDesc(Integer idProperty);

    Optional<PropertyDetails> findFirstByIdPropertyAndTipeProperty(Integer idProperty, String tipeProperty);

    List<PropertyDetails> findAllByIdPropertyOrderByTipePropertyAsc(Integer idProperty);

    Optional<PropertyDetails> findOneByTipeProperty(String tipeProperty);
}
