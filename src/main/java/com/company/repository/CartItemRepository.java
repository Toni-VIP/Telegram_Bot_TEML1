package com.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.company.model.CartItem;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByProductId(int productId);
}