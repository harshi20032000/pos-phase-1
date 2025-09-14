package com.hsw.pos_phase_1;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;


import static org.hamcrest.Matchers.hasSize;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc            // rollback DB changes after each test
class ProductIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    private String loadJson(String path) throws Exception {
        ClassPathResource r = new ClassPathResource(path);
        return StreamUtils.copyToString(r.getInputStream(), StandardCharsets.UTF_8);
    }

    @Test
    @Order(1)
    //.andDo(print()) use this to check response.
    void getAllProducts_2products() throws Exception {
        mockMvc.perform(get("/products"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("PRODUCTS_FETCHED"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.hasError").value(false))
                .andExpect(jsonPath("$.responsePayload").isArray())
                .andExpect(jsonPath("$.responsePayload", hasSize(2)))
                .andExpect(jsonPath("$.responsePayload[0].productId").isNumber())
                .andExpect(jsonPath("$.responsePayload[0].name").isNotEmpty());
    }

    @Test
    @Order(2)
    void createProduct_success() throws Exception {
        String requestBody = loadJson("mock/product-create.json");

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("PRODUCT_CREATED"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.hasError").value(false))
                .andExpect(jsonPath("$.responsePayload.productId").isNumber())
                .andExpect(jsonPath("$.responsePayload.name").value("oranges fresh grade ca"))
                .andExpect(jsonPath("$.responsePayload.price").value(600.0))
                .andExpect(jsonPath("$.responsePayload.stockQuantity").value(100))
                .andExpect(jsonPath("$.responsePayload.category").value("Fruits"))
                .andExpect(jsonPath("$.responsePayload.createdAt").isNotEmpty());
    }

    @Test
    @Order(3)
        //.andDo(print()) use this to check response.
    void getAllProducts_3products() throws Exception {
        mockMvc.perform(get("/products")).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("PRODUCTS_FETCHED"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.hasError").value(false))
                .andExpect(jsonPath("$.responsePayload").isArray())
                .andExpect(jsonPath("$.responsePayload", hasSize(3)));
    }

    @Test
    @Order(4)
    void getProductById_success() throws Exception {
        mockMvc.perform(get("/products/{id}", 3)) .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("PRODUCT_FETCHED"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.hasError").value(false))
                .andExpect(jsonPath("$.responsePayload.productId").isNumber())
                .andExpect(jsonPath("$.responsePayload.name").value("oranges fresh grade ca"))
                .andExpect(jsonPath("$.responsePayload.price").value(600.0))
                .andExpect(jsonPath("$.responsePayload.stockQuantity").value(100))
                .andExpect(jsonPath("$.responsePayload.category").value("Fruits"))
                .andExpect(jsonPath("$.responsePayload.createdAt").isNotEmpty());

    }

    @Test
    @Order(5)
    void updateProduct_success() throws Exception {
        String requestBody = loadJson("mock/product-update.json");
        mockMvc.perform(put("/products/{id}", 3)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("PRODUCT_UPDATED"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.hasError").value(false))
                .andExpect(jsonPath("$.responsePayload.productId").value(3))
                .andExpect(jsonPath("$.responsePayload.productId").isNumber())
                .andExpect(jsonPath("$.responsePayload.name").value("oranges fresh grade ca"))
                .andExpect(jsonPath("$.responsePayload.price").value(800.0))
                .andExpect(jsonPath("$.responsePayload.stockQuantity").value(6))
                .andExpect(jsonPath("$.responsePayload.category").value("Fruits"))
                .andExpect(jsonPath("$.responsePayload.createdAt").isNotEmpty());
    }

    @Test
    @Order(6)
    void deleteProduct_success() throws Exception {
        mockMvc.perform(delete("/products/{id}", 3))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("PRODUCT_DELETED"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.hasError").value(false))
                .andExpect(jsonPath("$.responsePayload").value("Product with ID 3 deleted successfully."));
    }

    @Test
    @Order(7)
        //.andDo(print()) use this to check response.
    void getAllProducts_2products_postdelete() throws Exception {
        mockMvc.perform(get("/products"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("PRODUCTS_FETCHED"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.hasError").value(false))
                .andExpect(jsonPath("$.responsePayload").isArray())
                .andExpect(jsonPath("$.responsePayload", hasSize(2)))
                .andExpect(jsonPath("$.responsePayload[0].productId").isNumber())
                .andExpect(jsonPath("$.responsePayload[0].name").isNotEmpty());
    }


}

