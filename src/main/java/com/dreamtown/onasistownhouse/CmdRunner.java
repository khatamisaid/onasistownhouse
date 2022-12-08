package com.dreamtown.onasistownhouse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dreamtown.onasistownhouse.entity.Role;
import com.dreamtown.onasistownhouse.entity.User;
import com.dreamtown.onasistownhouse.repository.RoleRepository;
import com.dreamtown.onasistownhouse.repository.UserRepository;

@Component
public class CmdRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CmdRunner.class);

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository UserRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        Role adminRole = new Role(1, "Administrator");
        Role produserRole = new Role(2, "Produser");
        Role editorRole = new Role(3, "Editor");
        Role uploaderRole = new Role(4, "Uploader");
        Role libraryRole = new Role(5, "Library");
        roleRepository.save(adminRole);
        roleRepository.save(produserRole);
        roleRepository.save(editorRole);
        roleRepository.save(uploaderRole);
        roleRepository.save(libraryRole);
        UserRepository
                .save(new User(1, "Administrator", encoder.encode("admin123"), "admin@it-berita1.com", adminRole));
        UserRepository
                .save(new User(2, "Produser", encoder.encode("produser123"), "produser@berita1.com", produserRole));
        UserRepository.save(new User(3, "Editor", encoder.encode("editor123"), "editor@berita1.com", editorRole));
        UserRepository
                .save(new User(4, "Uploader", encoder.encode("uploader123"), "uploader@berita1.com", uploaderRole));
        UserRepository
                .save(new User(5, "Library", encoder.encode("library123"), "library@berita1.com", libraryRole));
        logger.info("User and Role Has been created");
    }
}
