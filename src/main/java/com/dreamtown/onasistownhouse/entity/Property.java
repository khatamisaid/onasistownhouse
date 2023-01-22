package com.dreamtown.onasistownhouse.entity;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "property")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Property extends DateAudit {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_property")
    private Integer idProperty;

    @Column(name = "property_name", unique = true)
    private String propertyName;

    @Column(name = "universitas", columnDefinition = "TEXT")
    private String universitas;

    @Column(name = "sekolah", columnDefinition = "TEXT")
    private String sekolah;

    @Column(name = "belanja", columnDefinition = "TEXT")
    private String belanja;

    @Column(name = "kuliner", columnDefinition = "TEXT")
    private String kuliner;

    @Column(name = "rumah_sakit", columnDefinition = "TEXT")
    private String rumahSakit;

    @Column(name = "lainnya", columnDefinition = "TEXT")
    private String lainnya;

    @Column(name = "property_banner")
    private String propertyBanner;

    @JoinColumn(name = "id_wilayah", referencedColumnName = "id_wilayah", insertable = true, updatable = true)
    @ManyToOne
    private MWilayah wilayah;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_property", referencedColumnName = "id_property")
    private List<PropertyDetails> listPropertyDetails;
}
