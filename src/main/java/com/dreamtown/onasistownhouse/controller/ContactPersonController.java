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
import com.dreamtown.onasistownhouse.repository.LogWhatsAppRepository;
import static org.springframework.http.MediaType.*;

@Controller
@RequestMapping(value = "/cp")
public class ContactPersonController {

    @Autowired
    private ContactPersonRepository cpRepository;

    @Autowired
    private LogWhatsAppRepository logWhatsAppRepository;

    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseEntity<ContactPerson> findById(@PathVariable Integer id) {
        return new ResponseEntity<>(cpRepository.findById(id).get(), HttpStatus.OK);
    }

    @RequestMapping(value = "/log_whatsapp", method = RequestMethod.GET)
    public ResponseEntity<List<LogWhatsApp>> get_log_whatsapp() {
        return new ResponseEntity<>(logWhatsAppRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/log_whatsapp", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Map> logWhatsApp(@RequestBody ContactPerson cp) {
        Map res = new HashMap();
        Optional<LogWhatsApp> tempLw = logWhatsAppRepository.findOneByNamaContact(cp.getNamaContact());
        if (tempLw.isEmpty()) {
            logWhatsAppRepository.save(new LogWhatsApp(null, 1, cp.getNomorTelpon(),
                    cp.getNamaContact()));
        } else {
            LogWhatsApp logWhatsApp = tempLw.get();
            logWhatsApp.setDihubungi(logWhatsApp.getDihubungi() + 1);
            logWhatsAppRepository.save(logWhatsApp);
        }
        res.put("messages", "Sukses Log WhatsApp");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
