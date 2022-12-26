package com.dreamtown.onasistownhouse.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dreamtown.onasistownhouse.entity.Property;
import com.dreamtown.onasistownhouse.repository.PropertyRepository;

@Controller
@RequestMapping(value = "/property")
public class PropertyController {

    @Autowired
    private PropertyRepository propertyRepository;

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public ResponseEntity<Map> postProperty(@RequestBody Property property) {
        Map response = new HashMap<>();
        propertyRepository.save(property);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/del", method = RequestMethod.DELETE)
    public ResponseEntity<Map> delPropertyById(@RequestParam Integer id) {
        Map response = new HashMap<>();
        if (!propertyRepository.existsById(id)) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        propertyRepository.deleteById(id);
        response.put("message", "Berhasil menghapus data");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
