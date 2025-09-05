package com.hsw.pos_phase_1.controller;

import com.hsw.pos_phase_1.entities.Product;
import com.hsw.pos_phase_1.models.BaseUIResponse;
import com.hsw.pos_phase_1.service.ProductService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<BaseUIResponse<List<Product>>> getAllProducts() {
        List<Product> allProducts = productService.getAllProducts();

        BaseUIResponse<List<Product>> response = new BaseUIResponse<>();
        response.setStatus("SUCCESS");
        response.setCode("PRODUCTS_FETCHED");
        response.setMessage("Products fetched successfully");
        response.setResponsePayload(allProducts);

        return ResponseEntity.ok(response);
    }



    @GetMapping("/{id}")
    public ResponseEntity<BaseUIResponse<Product>> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id); // throws exception if not found

        BaseUIResponse<Product> response = new BaseUIResponse<>();
        response.setResponsePayload(product);
        response.setMessage("Product fetched successfully");
        response.setStatus("SUCCESS");
        response.setCode("PRODUCT_FETCHED");

        return ResponseEntity.ok(response);
    }



    @PostMapping
    public ResponseEntity<BaseUIResponse<Product>> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);

        BaseUIResponse<Product> response = new BaseUIResponse<>();
        response.setResponsePayload(createdProduct);
        response.setMessage("Product created successfully");
        response.setStatus("SUCCESS");
        response.setCode("PRODUCT_CREATED");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    @PutMapping("/{id}")
    public ResponseEntity<BaseUIResponse<Product>> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product) {

        Product updatedProduct = productService.updateProduct(id, product);

        BaseUIResponse<Product> response = new BaseUIResponse<>();
        response.setResponsePayload(updatedProduct);
        response.setMessage("Product updated successfully");
        response.setStatus("SUCCESS");
        response.setCode("PRODUCT_UPDATED");

        return ResponseEntity.ok(response);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<BaseUIResponse<String>> deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id); // throws if not found

        BaseUIResponse<String> response = new BaseUIResponse<>();
        response.setResponsePayload("Product with ID " + id + " deleted successfully.");
        response.setMessage("Product deleted successfully");
        response.setStatus("SUCCESS");
        response.setCode("PRODUCT_DELETED");

        return ResponseEntity.ok(response);
    }

}

