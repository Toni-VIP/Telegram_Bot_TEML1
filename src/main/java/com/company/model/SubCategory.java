package com.company.model;

import lombok.Getter;

@Getter
public enum SubCategory {
    // Parfyumlar uchun
    MENS_PERFUME("👔 Erkaklar atiri", MainCategory.PARFUMS),
    WOMENS_PERFUME("👗 Ayollar atiri", MainCategory.PARFUMS),
    UNISEX_PERFUME("🔄 Unisex atirlar", MainCategory.PARFUMS),
    MINI_PERFUME("🎁 Mini hajmlar", MainCategory.PARFUMS),
    PERFUME_SET("📦 Atr to'plamlari", MainCategory.PARFUMS),

    // Yuz uchun
    FACE_CREAM("🧴 Yuz kremlari", MainCategory.FACE),
    FACE_MASK("😷 Niqoblar", MainCategory.FACE),
    FACE_CLEANER("🧼 Tozalovchi vositalar", MainCategory.FACE),

    // Makiyaj uchun
    LIPSTICK("💄 Lablar uchun", MainCategory.MAKEUP),
    MASCARA("👁️ Ko'z uchun", MainCategory.MAKEUP),
    FOUNDATION("🎨 Tonal kremlar", MainCategory.MAKEUP),

    // Tana uchun
    BODY_CREAM("🧴 Tana kremlari", MainCategory.BODY),
    BODY_LOTION("🌸 Losyonlar", MainCategory.BODY),
    BODY_OIL("💧 Moylar", MainCategory.BODY),

    // Soch uchun
    SHAMPOO("🧴 Shampunlar", MainCategory.HAIR),
    HAIR_MASK("🎭 Soch niqoblari", MainCategory.HAIR),
    HAIR_OIL("💧 Soch moylari", MainCategory.HAIR),

    // Uy uchun
    HOME_PERFUME("🏠 Uy atiri", MainCategory.HOME),
    CANDLES("🕯️ Aromali shamlar", MainCategory.HOME),
    DIFFUSERS("🎋 Diffuzorlar", MainCategory.HOME),

    // Kiyimlar uchun
    CLOTHES_PERFUME("👕 Kiyim atiri", MainCategory.CLOTHES),
    FRESHENERS("🌸 Yangilatgichlar", MainCategory.CLOTHES);

    private final String displayName;
    private final MainCategory mainCategory;

    SubCategory(String displayName, MainCategory mainCategory) {
        this.displayName = displayName;
        this.mainCategory = mainCategory;
    }
}