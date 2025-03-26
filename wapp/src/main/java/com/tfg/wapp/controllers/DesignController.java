package com.tfg.wapp.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.tfg.wapp.entities.Ingredient;
import com.tfg.wapp.entities.Invoice;
import com.tfg.wapp.entities.Product;
import com.tfg.wapp.repositories.IngredientRepository;
import com.tfg.wapp.repositories.ProductRepository;


@Controller
@RequestMapping("/design")
@SessionAttributes("invoice")
public class DesignController {

  private final IngredientRepository ingredientRepo;
  private final ProductRepository productRepo;

  public DesignController(
        IngredientRepository ingredientRepo, 
        ProductRepository productRepo) {
    this.ingredientRepo = ingredientRepo;
    this.productRepo = productRepo;
  }

  @ModelAttribute
  public void addIngredientsAndProductToModel(Model model) {
    List<Ingredient> ingredients = new ArrayList<>();
    ingredientRepo.findAll().forEach(i -> ingredients.add(i));
    
    model.addAttribute("ingredientsList", ingredients);
    
    List<Product> products = new ArrayList<>();
    productRepo.findAll().forEach(p -> products.add(p));
    
    model.addAttribute("productsList", products);
  }

  @ModelAttribute(name = "invoice")
  public Invoice invoice() {
    return new Invoice();
  }

  @ModelAttribute(name = "ingredient")
  public Ingredient ingredient() {
    return new Ingredient();
  }
  
  @ModelAttribute(name = "product")
  public Ingredient product() {
    return new Ingredient();
  }

  @GetMapping
  public String showDesignForm() {
    return "design";
  }

  @PostMapping
  public String processFood(
      Invoice invoice) {

    return "redirect:/orders/pay";
  }



}

