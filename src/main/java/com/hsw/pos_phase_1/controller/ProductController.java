package com.hsw.pos_phase_1.controller;

import com.hsw.pos_phase_1.entities.Product;
import com.hsw.pos_phase_1.models.BaseUIResponse;
import com.hsw.pos_phase_1.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public BaseUIResponse<List<Product>> getAllProducts() {
        List<Product> allProducts = productService.getAllProducts();
        BaseUIResponse<List<Product>> baseUIResponse = new BaseUIResponse<>();
        baseUIResponse.setResponsePayload(allProducts);
        return baseUIResponse;
    }


    @GetMapping("/{id}")
    public BaseUIResponse<Product> getProductById(@PathVariable Long id) {
        BaseUIResponse<Product> baseUIResponse = new BaseUIResponse<>();
        baseUIResponse.setResponsePayload(productService.getProductById(id));
        return baseUIResponse;
    }


    @PostMapping
    public BaseUIResponse<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        BaseUIResponse<Product> baseUIResponse = new BaseUIResponse<>();
        baseUIResponse.setResponsePayload(createdProduct);
        return baseUIResponse;
    }


    @PutMapping("/{id}")
    public BaseUIResponse<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        BaseUIResponse<Product> baseUIResponse = new BaseUIResponse<>();
        baseUIResponse.setResponsePayload(updatedProduct);
        return baseUIResponse;
    }


    @DeleteMapping("/{id}")
    public BaseUIResponse<String> deleteProduct(@PathVariable Long id) {
        boolean isDeleted = productService.deleteProductById(id);
        BaseUIResponse<String> response = new BaseUIResponse<>();

        if (isDeleted) {
            response.setResponsePayload("Product with ID " + id + " deleted successfully.");
        } else {
            response.setResponsePayload("Failed to delete product with ID " + id + ".");
        }

        return response;
    }
}

