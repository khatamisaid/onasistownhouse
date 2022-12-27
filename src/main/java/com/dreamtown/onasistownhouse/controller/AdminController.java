package com.dreamtown.onasistownhouse.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.dreamtown.onasistownhouse.entity.Photo;
import com.dreamtown.onasistownhouse.entity.Property;
import com.dreamtown.onasistownhouse.entity.PropertyDetails;
import com.dreamtown.onasistownhouse.entity.Role;
import com.dreamtown.onasistownhouse.entity.User;
import com.dreamtown.onasistownhouse.repository.PhotoRepository;
import com.dreamtown.onasistownhouse.repository.PropertyDetailsRepository;
import com.dreamtown.onasistownhouse.repository.PropertyRepository;
import com.dreamtown.onasistownhouse.repository.UserRepository;
import com.dreamtown.onasistownhouse.utils.Menu;
import com.dreamtown.onasistownhouse.utils.UUIDGenerator;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private Menu menu;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PropertyDetailsRepository propertyDetailsRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private Environment env;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("username", httpSession.getAttribute("username"));
        model.addAttribute("menus", menu.getListProperty());
        return "admin/index";
    }

    @RequestMapping(value = "/managementUser", method = RequestMethod.GET)
    public String tambahUser(Model model) {
        model.addAttribute("username", httpSession.getAttribute("username"));
        model.addAttribute("menus", menu.getListProperty());
        return "admin/managementUser";
    }

    @RequestMapping(value = "/p/{propertyName}", method = RequestMethod.GET)
    public String findPropertyWithId(Model model, @PathVariable String propertyName) {
        Property property = propertyRepository.findOneByPropertyName(propertyName);
        model.addAttribute("username", httpSession.getAttribute("username"));
        model.addAttribute("menus", menu.getListProperty());
        model.addAttribute("property", property);
        return "admin/property";
    }

    @RequestMapping(value = "/p/{propertyName}/{id}", method = RequestMethod.GET)
    public String tambahPropertyDetails(Model model, @PathVariable String propertyName, @PathVariable Integer id) {
        PropertyDetails propertyDetails = propertyDetailsRepository.findById(id).get();
        model.addAttribute("propertyDetails", propertyDetails);
        return "admin/detailsProperty";
    }

    @RequestMapping(value = "/getUser/all", method = RequestMethod.GET)
    public ResponseEntity<Map> getUserAll(@RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "5") Integer length) {
        Pageable pageable = PageRequest.of(start, length, Sort.by("createdAt").descending());
        Role roleTemp = new Role();
        roleTemp.setIdRole(1);
        Page<User> pageUser = userRepository.findByRoleNot(roleTemp, pageable);
        Map res = new HashMap<>();
        res.put("data", pageUser);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @RequestMapping(value = "/property/{id}", method = RequestMethod.GET)
    public ResponseEntity<Property> getPropertyById(@PathVariable Integer id) {
        return new ResponseEntity<>(propertyRepository.findById(id).get(), HttpStatus.OK);
    }

    @RequestMapping(value = "/property", method = RequestMethod.POST)
    public ResponseEntity<Map> postProperty(@RequestBody Property property) {
        Map response = new HashMap<>();
        propertyRepository.save(property);
        response.put("message", "Property Berhasil di tambahkan");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/postPropertyDetails", method = RequestMethod.POST)
    public ResponseEntity<Map> postPropertyDetails(@RequestBody PropertyDetails propertyDetails) {
        Map response = new HashMap<>();
        propertyDetailsRepository.save(propertyDetails);
        response.put("message", "Property Berhasil di tambahkan");
        response.put("propertyDetails", propertyDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/deletePhoto",method = RequestMethod.DELETE)
    public ResponseEntity<Map> deletePhoto(@RequestParam Integer idPhoto){
        Map response = new HashMap<>();
        photoRepository.deleteById(idPhoto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/postListFoto", consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE }, method = RequestMethod.POST)
    public ResponseEntity<Map> postProperty(@RequestParam Integer idDetailsProperty, @RequestParam List<MultipartFile> files) {
        Map response = new HashMap<>();
        if (files.isEmpty()) {
            response.put("message", "Foto harus diisi");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        for(MultipartFile file : files){
            String[] splitFileName = file.getOriginalFilename().split("\\.");
            String extension = splitFileName[splitFileName.length - 1];
            String fileName = UUIDGenerator.generateType4UUID().toString() + "." + extension;
            File fileTemp = new File(env.getProperty("storage") + fileName);
            try{
                file.transferTo(fileTemp);
                Photo photo =  new Photo(null, fileName, idDetailsProperty);
                photoRepository.save(photo);
            }catch(IOException e){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        response.put("message", "Property Berhasil di tambahkan");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
