package com.ecommerce.app.controller;

import com.ecommerce.app.model.*;
import com.ecommerce.app.service.EcommerceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class EcommerceController {
    
    @Autowired
    private EcommerceService ecommerceService;
    
    // Product Endpoints
    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return ecommerceService.getAllProducts();
    }
    
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return ecommerceService.getProductById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/products")
    public Product createProduct(@Valid @RequestBody Product product) {
        return ecommerceService.createProduct(product);
    }
    
    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        try {
            Product updatedProduct = ecommerceService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            ecommerceService.deleteProduct(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Customer Endpoints
    @PostMapping("/customers")
    public Customer addCustomer(@Valid @RequestBody Customer customer) {
        return ecommerceService.addCustomer(customer);
    }
    
    // Cart Endpoints
    @GetMapping("/customers/{customerId}/cart")
    public ResponseEntity<Cart> getCart(@PathVariable Long customerId) {
        try {
            Cart cart = ecommerceService.getCartByCustomerId(customerId);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/customers/{customerId}/cart/items")
    public ResponseEntity<Cart> addProductToCart(
            @PathVariable Long customerId,
            @RequestBody Map<String, Object> request) {
        try {
            Long productId = Long.valueOf(request.get("productId").toString());
            Integer quantity = Integer.valueOf(request.get("quantity").toString());
            
            Cart cart = ecommerceService.addProductToCart(customerId, productId, quantity);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/customers/{customerId}/cart/items/{productId}")
    public ResponseEntity<Cart> removeProductFromCart(
            @PathVariable Long customerId, 
            @PathVariable Long productId) {
        try {
            Cart cart = ecommerceService.removeProductFromCart(customerId, productId);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/customers/{customerId}/cart/items/{productId}")
    public ResponseEntity<Cart> updateCart(
            @PathVariable Long customerId,
            @PathVariable Long productId,
            @RequestBody Map<String, Integer> request) {
        try {
            Integer quantity = request.get("quantity");
            Cart cart = ecommerceService.updateCart(customerId, productId, quantity);
            return ResponseEntity.ok(cart);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/customers/{customerId}/cart")
    public ResponseEntity<Void> emptyCart(@PathVariable Long customerId) {
        try {
            ecommerceService.emptyCart(customerId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Order Endpoints
    @PostMapping("/customers/{customerId}/orders")
    public ResponseEntity<Order> placeOrder(@PathVariable Long customerId) {
        try {
            Order order = ecommerceService.placeOrder(customerId);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable Long orderId) {
        return ecommerceService.getOrderById(orderId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/customers/{customerId}/orders")
    public List<Order> getAllOrdersForCustomer(@PathVariable Long customerId) {
        return ecommerceService.getAllOrdersForCustomer(customerId);
    }
}