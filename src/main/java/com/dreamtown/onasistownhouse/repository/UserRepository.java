package com.dreamtown.onasistownhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dreamtown.onasistownhouse.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
    
}
