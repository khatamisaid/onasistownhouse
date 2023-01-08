package com.dreamtown.onasistownhouse.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.dreamtown.onasistownhouse.entity.ContactPerson;
import com.dreamtown.onasistownhouse.entity.Photo;
import com.dreamtown.onasistownhouse.entity.Property;
import com.dreamtown.onasistownhouse.entity.PropertyDetails;
import com.dreamtown.onasistownhouse.entity.Video;
import com.dreamtown.onasistownhouse.repository.PropertyDetailsRepository;
import com.dreamtown.onasistownhouse.repository.PropertyRepository;
import com.dreamtown.onasistownhouse.repository.PropertyStatusRepository;
import com.dreamtown.onasistownhouse.utils.UUIDGenerator;

@Controller
@RequestMapping(value = "/property")
public class PropertyController {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PropertyDetailsRepository propertyDetailsRepository;

    @Autowired
    private Environment env;

    @Autowired
    private PropertyStatusRepository propertyStatusRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String property() {
        return "property";
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public ResponseEntity<Map> postProperty(@RequestPart Property property, @RequestPart MultipartFile file)
            throws IllegalStateException, IOException {
        Map response = new HashMap<>();
        if (file.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        String[] splitFileName = file.getOriginalFilename().split("\\.");
        String extension = splitFileName[splitFileName.length - 1];
        String fileName = UUIDGenerator.generateType4UUID().toString() + "." + extension;
        File fileTemp = new File(env.getProperty("storage.images") + fileName);
        file.transferTo(fileTemp);
        property.setPropertyBanner(fileName);
        propertyRepository.save(property);
        response.put("message", "Property Berhasil di tambahkan");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/post/{idProperty}", method = RequestMethod.POST)
    public ResponseEntity<Map> postDetailsProperty(@PathVariable Integer idProperty,
            @RequestPart PropertyDetails propertyDetails, @RequestPart(required = false) List<MultipartFile> images,
            @RequestPart(required = false) List<MultipartFile> videos)
            throws IllegalStateException, IOException {
        Map response = new HashMap<>();
        propertyDetails.setIdProperty(idProperty);
        if (!images.isEmpty()) {
            List<Photo> listPhoto = new ArrayList<>();
            for (MultipartFile file : images) {
                String[] splitFileName = file.getOriginalFilename().split("\\.");
                String extension = splitFileName[splitFileName.length - 1];
                String fileName = UUIDGenerator.generateType4UUID().toString() + "." + extension;
                File fileTemp = new File(env.getProperty("storage.images") + fileName);
                try {
                    file.transferTo(fileTemp);
                    Photo photo = new Photo(null, fileName, propertyDetails.getIdDetailsProperty());
                    listPhoto.add(photo);
                } catch (IOException e) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
            propertyDetails.setListPhoto(listPhoto);
        }
        if (!videos.isEmpty()) {
            List<Video> listVideo = new ArrayList<>();
            for (MultipartFile file : videos) {
                String[] splitFileName = file.getOriginalFilename().split("\\.");
                String extension = splitFileName[splitFileName.length - 1];
                String fileName = UUIDGenerator.generateType4UUID().toString() + "." + extension;
                File fileTemp = new File(env.getProperty("storage.videos") + fileName);
                try {
                    file.transferTo(fileTemp);
                    Video photo = new Video(null, fileName, propertyDetails.getIdDetailsProperty());
                    listVideo.add(photo);
                } catch (IOException e) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
            propertyDetails.setListVideo(listVideo);
        }
        propertyDetailsRepository.save(propertyDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/update/{idProperty}", method = RequestMethod.POST)
    public ResponseEntity<Map> updateDetailsProperty(@PathVariable Integer idProperty,
            @RequestPart PropertyDetails propertyDetails, @RequestPart List<MultipartFile> images,
            @RequestPart List<MultipartFile> videos, @RequestPart List<ContactPerson> contactPersons)
            throws IllegalStateException, IOException {
        Map response = new HashMap<>();
        propertyDetailsRepository.save(propertyDetails);
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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Map> findByIdAndTipeProperty(@PathVariable Integer id, @RequestParam String tipeProperty) {
        Map response = new HashMap<>();
        response.put("property", propertyRepository.findById(id).get());
        response.put("propertyDetails",
                propertyDetailsRepository.findOneByIdDetailsPropertyAndTipeProperty(id, tipeProperty));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
