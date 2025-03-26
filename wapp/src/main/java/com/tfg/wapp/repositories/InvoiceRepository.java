package com.tfg.wapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tfg.wapp.entities.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer>{

}
