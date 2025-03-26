package com.tfg.wapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tfg.wapp.entities.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

}
