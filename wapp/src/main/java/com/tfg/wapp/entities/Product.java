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
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(
			nullable = false,
			unique = true)
	private String name;
	
	@Column(
			nullable = false)
	private double price;
	
//	@OneToMany(mappedBy = "product")
//	private Set<Invoice> invoices;

}
