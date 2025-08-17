package com.ecommerce.app.service;

import com.ecommerce.app.model.*;
import com.ecommerce.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
@Transactional
public class EcommerceService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    // Product Methods
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
    
    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ürün bulunamadı: " + id));
            
        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        product.setStock(productDetails.getStock());
        product.setDescription(productDetails.getDescription());
        
        return productRepository.save(product);
    }
    
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    // Customer Methods
    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
    
    // Cart Methods
    public Cart getCartByCustomerId(Long customerId) {
        return cartRepository.findByCustomerId(customerId)
            .orElseGet(() -> {
                Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Müşteri bulunamadı: " + customerId));
                return cartRepository.save(new Cart(customer));
            });
    }
    
    public Cart addProductToCart(Long customerId, Long productId, Integer quantity) {
        Cart cart = getCartByCustomerId(customerId);
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Ürün bulunamadı: " + productId));
            
        if (product.getStock() < quantity) {
            throw new RuntimeException("Yeterli stok yok. Mevcut stok: " + product.getStock());
        }
        
        cart.addItem(product, quantity);
        return cartRepository.save(cart);
    }
    
    public Cart removeProductFromCart(Long customerId, Long productId) {
        Cart cart = getCartByCustomerId(customerId);
        cart.removeItem(productId);
        return cartRepository.save(cart);
    }
    
    public Cart updateCart(Long customerId, Long productId, Integer quantity) {
        Cart cart = getCartByCustomerId(customerId);
        
        cart.getItems().stream()
            .filter(item -> item.getProduct().getId().equals(productId))
            .findFirst()
            .ifPresent(item -> {
                if (quantity <= 0) {
                    cart.removeItem(productId);
                } else {
                    item.setQuantity(quantity);
                }
            });
            
        return cartRepository.save(cart);
    }
    
    public void emptyCart(Long customerId) {
        Cart cart = getCartByCustomerId(customerId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
    
    // Order Methods
    public Order placeOrder(Long customerId) {
        Cart cart = getCartByCustomerId(customerId);
        
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Sepet boş, sipariş verilemez");
        }
        
        // Stok kontrolü
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Yetersiz stok: " + product.getName());
            }
        }
        
        // Stokları güncelle
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }
        
        // Sipariş oluştur
        Order order = new Order(cart.getCustomer(), cart.getTotalPrice(), new ArrayList<>(cart.getItems()));
        order = orderRepository.save(order);
        
        // Sepeti temizle
        emptyCart(customerId);
        
        return order;
    }
    
    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }
    
    public List<Order> getAllOrdersForCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
}