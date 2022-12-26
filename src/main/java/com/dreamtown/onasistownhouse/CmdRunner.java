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
import com.dreamtown.onasistownhouse.entity.PropertyType;
import com.dreamtown.onasistownhouse.entity.Role;
import com.dreamtown.onasistownhouse.entity.User;
import com.dreamtown.onasistownhouse.repository.PropertyDetailsRepository;
import com.dreamtown.onasistownhouse.repository.PropertyRepository;
import com.dreamtown.onasistownhouse.repository.PropertyTypeRepository;
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
        private PropertyRepository propertyRepository;

        @Autowired
        private PropertyTypeRepository propertyTypeRepository;

        @Autowired
        private PropertyDetailsRepository propertyDetailsRepository;

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
                                                "superadmin@onasistownhouse.com",
                                                superAdminRole));
                UserRepository
                                .save(new User(2, "admin", encoder.encode("onasisadmin123"), "onasisadmin123",
                                                "admin@onasistownhouse.com",
                                                adminRole));
                UserRepository.save(
                                new User(3, "marketing", encoder.encode("onasismarketing321"), "onasismarketing321",
                                                "marketing@onasistownhouse.com",
                                                marketingRole));
                logger.info("User and Role Has been created");

                Double harga = 2000000000.0;

                PropertyDetails propertyDetails = new PropertyDetails(1, 35, 32, 2, 1, "Jalan Raya", "", harga,
                                new ArrayList<>(),
                                new ArrayList<>(), new ArrayList<>());
                propertyDetailsRepository.save(propertyDetails);
                PropertyType propertyTypeA = new PropertyType(1, 1, "A", propertyDetails);
                List<PropertyType> listPropertyType = new ArrayList<>();
                listPropertyType.add(propertyTypeA);
                Property property1 = new Property(1, "Mawar", listPropertyType);
                propertyRepository.save(property1);
                // Property property2 = new Property(2, "Melati", harga, new ArrayList<>());
                // propertyRepository.save(property2);
                // Property property3 = new Property(3, "Kamboja", harga, new ArrayList<>());
                // propertyRepository.save(property3);
                // Property property4 = new Property(4, "Taman Anggrek", harga, new
                // ArrayList<>());
                // propertyRepository.save(property4);
                // Property property5 = new Property(5, "Cempaka Putih", harga, new
                // ArrayList<>());
                // propertyRepository.save(property5);
                logger.info("Property Has been created");

        }
}
