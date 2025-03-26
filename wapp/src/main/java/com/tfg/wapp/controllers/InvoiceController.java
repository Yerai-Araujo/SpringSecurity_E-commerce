package com.tfg.wapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.tfg.wapp.entities.Invoice;
import com.tfg.wapp.repositories.InvoiceRepository;

@Controller
@RequestMapping("/orders")
@SessionAttributes("invoice")
public class InvoiceController {
	
	private final InvoiceRepository invoiceRepository;
	
	public InvoiceController(InvoiceRepository invoiceRepo) {
		this.invoiceRepository = invoiceRepo;
	}

	@GetMapping("/pay")
	  public String orderForm() {
	    return "orderForm";
	  }
	
	@PostMapping
	public String processOrder(Invoice invoice, SessionStatus sessionStatus) {
		
		invoice.setTotal(invoice.getProduct().getPrice() + invoice.getIngredient().getPrice());
		invoiceRepository.save(invoice);
		sessionStatus.setComplete();
		
		return "redirect:/home";
	}
}
