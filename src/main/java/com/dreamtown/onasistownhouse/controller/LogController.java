package com.dreamtown.onasistownhouse.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dreamtown.onasistownhouse.entity.ContactPerson;
import com.dreamtown.onasistownhouse.entity.LogWhatsApp;
import com.dreamtown.onasistownhouse.repository.ContactPersonRepository;
import com.dreamtown.onasistownhouse.repository.LogAktivitasRepository;
import com.dreamtown.onasistownhouse.repository.LogWhatsAppRepository;
import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/log")
public class LogController {

    @Autowired
    private ContactPersonRepository cpRepository;

    @Autowired
    private LogWhatsAppRepository logWhatsAppRepository;

    @Autowired
    private LogAktivitasRepository logAktivitasRepository;

    @RequestMapping(value = "/whatsapp/findById/{id}", method = RequestMethod.GET)
    public ResponseEntity<ContactPerson> findById(@PathVariable Integer id) {
        return new ResponseEntity<>(cpRepository.findById(id).get(), HttpStatus.OK);
    }

    @RequestMapping(value = "/whatsapp", method = RequestMethod.GET)
    public ResponseEntity<List<Object[]>> get_log_whatsapp(String tglDari, String tglSampai) {
        return new ResponseEntity<>(logWhatsAppRepository.logWhatsApp(tglDari.replace("-", ""),
                tglSampai.replace("-", "")), HttpStatus.OK);
    }

    @RequestMapping(value = "/aktivitas", method = RequestMethod.GET)
    public ResponseEntity<Map> get_log_aktivitas(String tglDari, String tglSampai) {
        Map res = new HashMap<>();
        res.put("totalKeseluruhan", logAktivitasRepository.findAll().size());
        res.put("totalHariIni", logAktivitasRepository.logHariBetween(tglDari.replace("-", ""),
                tglSampai.replace("-", "")));
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @RequestMapping(value = "/whatsapp", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Map> logWhatsApp(@RequestBody ContactPerson cp) {
        Map res = new HashMap();
        logWhatsAppRepository.save(new LogWhatsApp(null, cp.getNomorTelpon(),
                cp.getNamaContact()));
        res.put("messages", "Sukses Log WhatsApp");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
