package com.invoice.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.invoice.model.Product;

public interface ProductRepo extends JpaRepository<Product, String> {

}
