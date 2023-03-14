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
@Table(name = "property_details")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PropertyDetails extends DateAudit {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_details_property")
    private Integer idDetailsProperty;

    @Column(name = "id_property")
    private Integer idProperty;

    @Column(name = "luas_tanah")
    private Integer luasTanah;

    @Column(name = "luas_bangunan")
    private Integer luasBangunan;

    @Column(name = "kamar_tidur")
    private Integer kamarTidur;

    @Column(name = "kamar_mandi")
    private Integer kamarMandi;

    @Column(name = "car_port")
    private Integer carPort;

    @Column(name = "lokasi", columnDefinition = "TEXT")
    private String lokasi;

    @Column(name = "deskripsi", columnDefinition = "TEXT")
    private String deskripsi;

    @Column(name = "tipe_property")
    private String tipeProperty;

    @Column(name = "harga")
    private Double harga;

    @JoinColumn(name = "id_property_status", referencedColumnName = "id_property_status", insertable = true, updatable = true)
    @ManyToOne
    private PropertyStatus propertyStatus;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_details_property", referencedColumnName = "id_details_property")
    private List<Photo> listPhoto;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_details_property", referencedColumnName = "id_details_property")
    private List<Video> listVideo;
}
