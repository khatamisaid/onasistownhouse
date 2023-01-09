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
    private String tanggal;
    private String namaProperty;
    private String tipeProperty;
    private String luasBangunan;
    private String luasTanah;
    private String namaPembeli;
    private String noKtpPembeli;
    private String alamatLengkap;
    private String noTelepon;
    private String caraBayar;
    private String hargaProperty;
    private String discount;
    private String hargaSetelahDiscount;
    private String bookingFee;
    private String tanggalBooking;
    private String dp;
    private String hargaDp;
    private String totalHarga;
    private Boolean periodeCicilanIsTrue;
    private String periodeCicilan;
    private String cicilan;
    private Boolean kenaAdmin;
    private String biayaAdmin;
}
