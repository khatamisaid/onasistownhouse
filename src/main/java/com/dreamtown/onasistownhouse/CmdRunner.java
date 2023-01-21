package com.dreamtown.onasistownhouse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dreamtown.onasistownhouse.entity.Property;
import com.dreamtown.onasistownhouse.entity.PropertyDetails;
import com.dreamtown.onasistownhouse.entity.PropertyStatus;
import com.dreamtown.onasistownhouse.entity.Role;
import com.dreamtown.onasistownhouse.entity.User;
import com.dreamtown.onasistownhouse.entity.Website;
import com.dreamtown.onasistownhouse.entity.WebsitePhoto;
import com.dreamtown.onasistownhouse.repository.PropertyDetailsRepository;
import com.dreamtown.onasistownhouse.repository.PropertyRepository;
import com.dreamtown.onasistownhouse.repository.PropertyStatusRepository;
import com.dreamtown.onasistownhouse.repository.RoleRepository;
import com.dreamtown.onasistownhouse.repository.UserRepository;
import com.dreamtown.onasistownhouse.repository.WebsiteRepository;

@Component
public class CmdRunner implements CommandLineRunner {

        private static final Logger logger = LoggerFactory.getLogger(CmdRunner.class);

        @Autowired
        private RoleRepository roleRepository;

        @Autowired
        private UserRepository UserRepository;

        @Autowired
        private WebsiteRepository websiteRepository;

        @Autowired
        private PropertyStatusRepository propertyStatusRepository;

        @Autowired
        private PasswordEncoder encoder;

        @Override
        public void run(String... args) throws Exception {
                Role superAdminRole = new Role(1, "Super User");
                Role adminRole = new Role(2, "Admin");
                Role marketingRole = new Role(3, "Marketing");
                roleRepository.save(superAdminRole);
                roleRepository.save(adminRole);
                roleRepository.save(marketingRole);
                UserRepository
                                .save(new User(1, "superadmin", encoder.encode("123456"), "123456",
                                                superAdminRole));
                UserRepository
                                .save(new User(2, "admin", encoder.encode("onasisadmin123"), "onasisadmin123",
                                                adminRole));
                UserRepository.save(
                                new User(3, "marketing", encoder.encode("onasismarketing321"), "onasismarketing321",
                                                marketingRole));
                logger.info("User and Role Has been created");

                // Add Property Status

                List<PropertyStatus> listPropertyStatus = new ArrayList<>();

                listPropertyStatus.add(new PropertyStatus(1, "Available"));
                listPropertyStatus.add(new PropertyStatus(2, "Hold"));
                listPropertyStatus.add(new PropertyStatus(3, "Sold"));
                propertyStatusRepository.saveAll(listPropertyStatus);
                logger.info("Property Status Has been created");

                // add Website title 
                List<WebsitePhoto> listWebsitePhoto = new ArrayList<>();
                listWebsitePhoto.add(new WebsitePhoto(1, "12.png", null));
                listWebsitePhoto.add(new WebsitePhoto(2, "14.png", null));
                listWebsitePhoto.add(new WebsitePhoto(3, "15.png", null));
                websiteRepository.save(new Website(1, "Onasis Town House", "video3.mp4", listWebsitePhoto));

                logger.info("website tittle has been created");

                // Double harga = 1200000000.0;
                // PropertyDetails propertyDetails1 = new PropertyDetails(1, 1, 35, 40, 2, 1,
                // "Jalan Pemuda", "", "A",
                // harga, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                // List<PropertyDetails> listPropertyDetails = new ArrayList<>();
                // listPropertyDetails.add(propertyDetails1);
                // Property property = new Property(1, "Mawar", "smart_home_jlpemuda.jpg" ,
                // listPropertyDetails);
                // propertyRepository.save(property);
                // logger.info("Property Has been created");

        }
}
