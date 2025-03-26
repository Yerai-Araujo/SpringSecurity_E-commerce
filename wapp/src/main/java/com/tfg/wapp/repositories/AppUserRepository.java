package com.tfg.wapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tfg.wapp.entities.AppUser;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);
    
}
