package com.dreamtown.onasistownhouse.viewmodel;

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
    private String namaProperty;
    private String tipeProperty;
    private String tglHariIni;
    private String caraBayar;
    private Boolean statusCaraBayar;
    private String discount;
    private String hargaProperty;
    private String dp;
    private String tglDp;
    private String totalAngsuran;
    private String bookingFee;
    private String tglBookingFee;
    private Boolean kenaAdmin;
    private String cicilan;
    private String cicilanKe;
    private String nominal;
    private String tglCicilan;
    private String namaMarketing;
    private String namaPembeli;
    private String noKtp;
    private String noTelpon;
    private String alamatPembeli;
    private String catatanPembeli;
    private Boolean dataPembeli;
    private String luasTanah;
    private String luasBangunan;
    private String tglAngsuran;
}
