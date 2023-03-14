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
public class ViewModelTambahKontak {
    private Integer idContactPerson;
    private String nomorTelpon;
    private String namaContact;
}
