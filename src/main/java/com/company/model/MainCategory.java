package com.company.model;

import lombok.Getter;

@Getter
public enum MainCategory {
    PARFUMS("🌺 Parfyumlar (Atirlar)"),
    FACE("👩 Yuz uchun parvarish"),
    MAKEUP("💄 Makiyaj uchun"),
    BODY("🧴 Tana uchun"),
    HAIR("💇‍♀️ Soch uchun"),
    HOME("🏠 Uy uchun"),
    CLOTHES("👔 Kiyimlar uchun");

    private final String displayName;

    MainCategory(String displayName) {
        this.displayName = displayName;
    }
}