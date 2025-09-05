package com.hsw.pos_phase_1.service;

import com.hsw.pos_phase_1.entities.Product;
import com.hsw.pos_phase_1.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    
    public Product updateProduct(Long id, Product product) {
        Product fetchedProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        fetchedProduct.setName(product.getName());
        fetchedProduct.setPrice(product.getPrice());
        fetchedProduct.setStockQuantity(product.getStockQuantity());
        fetchedProduct.setCategory(product.getCategory());

        return productRepository.save(fetchedProduct);
    }

    
    public boolean deleteProductById(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}