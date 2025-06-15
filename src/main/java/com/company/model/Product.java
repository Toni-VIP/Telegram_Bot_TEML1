package com.company.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private int id;
    private String name;
    private int price;
    private String imageUrl;
    private String description;
    
    @Enumerated(EnumType.STRING)
    private SubCategory subCategory;

    public MainCategory getMainCategory() {
        return subCategory.getMainCategory();
    }
}