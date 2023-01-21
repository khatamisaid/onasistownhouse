package com.dreamtown.onasistownhouse.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dreamtown.onasistownhouse.entity.Role;
import com.dreamtown.onasistownhouse.entity.User;
import com.dreamtown.onasistownhouse.repository.RoleRepository;
import com.dreamtown.onasistownhouse.repository.UserRepository;
import com.dreamtown.onasistownhouse.viewmodel.ViewModelUser;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<Map> getUserAll(@RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "5") Integer length) {
        Pageable pageable = PageRequest.of(start, length, Sort.by("createdAt").descending());
        Page<User> pageUser = userRepository.findAll(pageable);
        Map res = new HashMap<>();
        res.put("data", pageUser);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Map> postUser(@RequestBody ViewModelUser user) {
        Map res = new HashMap<>();
        if (user.getIdUser() != null) {
            res.put("message", "Berhasil mengupdate data user");
        } else {
            res.put("message", "Berhasil menambahkan user");
        }
        userRepository.save(new User(user.getIdUser(), user.getUsername(), encoder.encode(user.getPassword()), user.getPassword(), roleRepository.findById(user.getRole()).get())); 
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseEntity<Map> findById(@PathVariable Integer id) {
        Map res = new HashMap<>();
        res.put("data", userRepository.findById(id).get());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
