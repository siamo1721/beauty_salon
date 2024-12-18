package com.example.Kurs_salon.service;

import com.example.Kurs_salon.model.Product;
import com.example.Kurs_salon.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
    
    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
    }
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public Product updateStock(Long id, Integer quantity) {
        Product product = getProductById(id);
        product.setStockQuantity(quantity);
        return productRepository.save(product);
    }
}