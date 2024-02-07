package com.invoice.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoice.model.Product;
import com.invoice.service.PdfService;
import com.invoice.service.ProductService;

@RestController
@RequestMapping(path = "/pdf")
public class ProductController {

	@Autowired
	private PdfService pdfService;

	@Autowired
	private ProductService prodService;

	@GetMapping(path = "/send")
	public ResponseEntity<String> sendPdf() throws MalformedURLException, URISyntaxException, IOException {
		pdfService.createPdf();
		return new ResponseEntity<String>("Invoice created", HttpStatus.OK);
	}

	@PostMapping(path = "/product")
	public ResponseEntity<Product> createProduct(@RequestBody Product prod) {
		Product createProduct = this.prodService.createProduct(prod);
		return new ResponseEntity<Product>(createProduct, HttpStatus.CREATED);
	}
}
