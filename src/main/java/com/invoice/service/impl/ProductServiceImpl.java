package com.invoice.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.invoice.model.Product;
import com.invoice.repo.ProductRepo;
import com.invoice.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepo prodRepo;

	@Override
	public Product createProduct(Product prod) {
		String randomProdId = UUID.randomUUID().toString();
		Product prod1 = Product.builder().prodId(randomProdId).name(prod.getName()).quantity(prod.getQuantity())
				.amount(prod.getAmount()).tax(prod.getTax()).build();
		Product savedProduct = this.prodRepo.save(prod1);
		return savedProduct;
	}

	@Override
	public Product getProduct(String prodId) {
		Product product = this.prodRepo.findById(prodId).orElseThrow(() -> new RuntimeException("Resource not found"));
		return product;
	}

	@Override
	public List<Product> getAllProducts() {
		List<Product> allProd = this.prodRepo.findAll();
		return allProd;
	}

}
