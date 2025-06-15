package com.company.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.company.repository.ProductRepository;
import com.company.repository.CartItemRepository;
import com.company.model.*;
import java.util.List;

@Service
public class BotService {
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;

    public List<Product> getProductsBySubCategory(SubCategory subCategory) {
        return productRepository.findBySubCategory(subCategory);
    }

    public void addToCart(int productId, int quantity) {
        CartItem cartItem = new CartItem();
        cartItem.setProductId(productId);
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }
}