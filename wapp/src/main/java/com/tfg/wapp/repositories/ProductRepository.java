package com.tfg.wapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tfg.wapp.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{

}
