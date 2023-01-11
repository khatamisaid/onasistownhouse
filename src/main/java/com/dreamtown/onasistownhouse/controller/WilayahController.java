package com.dreamtown.onasistownhouse.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dreamtown.onasistownhouse.entity.MWilayah;
import com.dreamtown.onasistownhouse.repository.MWilayahRepository;

@Controller
@RequestMapping(value = "/wilayah")
public class WilayahController {
    
    @Autowired
    private MWilayahRepository mWilayahRepository;

    @RequestMapping("")
    public ResponseEntity<Map> tambahWilayah(@RequestParam String namaWilayah){
        Map res = new HashMap<>();
        mWilayahRepository.save(new MWilayah(null, namaWilayah));
        res.put("message", "Wilayah Berhasil ditambahkan");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
