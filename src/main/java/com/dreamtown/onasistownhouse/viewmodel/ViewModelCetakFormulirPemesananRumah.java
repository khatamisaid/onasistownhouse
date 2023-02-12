package com.dreamtown.onasistownhouse.viewmodel;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ViewModelCetakFormulirPemesananRumah {
    private String tanggal;
    private String namaProperty;
    private List<JasperP1> jasperP1;
    private List<JasperP2> jasperP2;
    private Boolean periodeCicilanIsTrue;
}
