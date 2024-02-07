package com.invoice.service;

import java.util.List;

import com.invoice.model.Product;

public interface ProductService {

	Product createProduct(Product prod);

	Product getProduct(String prodId);

	List<Product> getAllProducts();
}
