package com.tfg.wapp.entities;


import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(
			nullable = false)
	private double total;
	
	@ManyToOne()
	@JoinColumn(name = "product_id")
	private Product product;
	
	@ManyToOne()
	@JoinColumn(name = "ingredient_id")
	private Ingredient ingredient;
}
