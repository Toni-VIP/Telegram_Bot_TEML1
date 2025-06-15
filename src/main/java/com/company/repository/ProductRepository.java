package com.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.company.model.Product;
import com.company.model.SubCategory;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findBySubCategory(SubCategory subCategory);
}