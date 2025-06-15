package com.company;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.*;
import java.util.stream.Collectors;

public class MyBot extends TelegramLongPollingBot {

    // Mahsulotlar ro'yxati
    private final List<Product> products = Arrays.asList(
            // PARFUMS category
            new Product(1, "Chanel Blue", 450000, "https://i.imgur.com/parfum1.jpg",
                    "💫 Erkaklar uchun premium atir\n🏭 Original\n⚖️ 100ml\n📦 Optom narx: 400,000", SubCategory.MENS_PERFUME),
            new Product(2, "Dior J'adore", 520000, "https://i.imgur.com/parfum2.jpg",
                    "💫 Ayollar uchun premium atir\n🏭 Original\n⚖️ 100ml\n📦 Optom narx: 470,000", SubCategory.WOMENS_PERFUME),
            new Product(3, "Versace Eros", 380000, "https://i.imgur.com/parfum3.jpg",
                    "💫 Unisex atir\n🏭 Original\n⚖️ 100ml\n📦 Optom narx: 350,000", SubCategory.UNISEX_PERFUME),

            // FACE category
            new Product(4, "The Ordinary Serum", 180000, "https://i.imgur.com/face1.jpg",
                    "🧴 Yuz uchun serum\n✨ Vitamin C\n⚖️ 30ml\n📦 Optom narx: 150,000", SubCategory.FACE_CREAM),
            new Product(5, "Clinique Cream", 250000, "https://i.imgur.com/face2.jpg",
                    "🧴 Namlovchi krem\n✨ 72h hydration\n⚖️ 50ml\n📦 Optom narx: 220,000", SubCategory.FACE_CREAM),

            // MAKEUP category
            new Product(6, "MAC Lipstick", 150000, "https://i.imgur.com/makeup1.jpg",
                    "💄 Matt lablar uchun\n🎨 Ruby Woo\n⚖️ 3.5g\n📦 Optom narx: 130,000", SubCategory.LIPSTICK),
            new Product(7, "Maybelline Mascara", 120000, "https://i.imgur.com/makeup2.jpg",
                    "👁️ Suv o'tkazmaydigan\n⚫ Qora rang\n⚖️ 10ml\n📦 Optom narx: 100,000", SubCategory.MASCARA),

            // BODY category
            new Product(8, "Nivea Body Milk", 80000, "https://i.imgur.com/body1.jpg",
                    "🧴 Tana uchun sut\n✨ 48h namlik\n⚖️ 400ml\n📦 Optom narx: 70,000", SubCategory.BODY_LOTION),
            new Product(9, "Bio Oil", 120000, "https://i.imgur.com/body2.jpg",
                    "💧 Universal moy\n✨ Stretch belgilar uchun\n⚖️ 200ml\n📦 Optom narx: 100,000", SubCategory.BODY_OIL),

            // HAIR category
            new Product(10, "L'Oreal Shampoo", 90000, "https://i.imgur.com/hair1.jpg",
                    "🧴 Professional shampun\n✨ Barcha soch turlari uchun\n⚖️ 500ml\n📦 Optom narx: 80,000", SubCategory.SHAMPOO),

            // HOME category
            new Product(11, "Zara Home Diffuser", 200000, "https://i.imgur.com/home1.jpg",
                    "🏠 Xona uchun diffuzor\n🌸 Black Vanilla\n⚖️ 200ml\n📦 Optom narx: 180,000", SubCategory.DIFFUSERS),

            // CLOTHES category
            new Product(12, "Lenor Fresh", 45000, "https://i.imgur.com/clothes1.jpg",
                    "👕 Kiyimlar uchun yangilatgich\n✨ Spring aroma\n⚖️ 250ml\n📦 Optom narx: 40,000", SubCategory.FRESHENERS)
    );

    // Savat (har bir foydalanuvchi uchun)
    private final Map<Long, List<CartItem>> userCarts = new HashMap<>();

    public MyBot() {
        super("7919838399:AAHJZBOudGOlzWov-vpT0RLNXSkz7Kok7JI");
    }

    @Override
    public String getBotUsername() {
        return "https://t.me/Optom_parfyum_bot";
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleTextMessage(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        }
    }

    @SneakyThrows
    private void handleTextMessage(Message message) {
        long chatId = message.getChatId();
        String text = message.getText();

        switch (text) {
            case "/start":
                sendWelcomeMessage(chatId);
                break;
            case "🛍️ Mahsulotlar":
                showCategories(chatId);
                break;
            case "🛒 Savat":
                showCart(chatId);
                break;
            case "📞 Aloqa":
                showContact(chatId);
                break;
            case "ℹ️ Ma'lumot":
                showInfo(chatId);
                break;
            default:
                sendMessage(chatId, "Iltimos, menyudan tanlang 👇");
        }
    }

    @SneakyThrows
    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();

        if (data.startsWith("main_category_")) {
            String categoryName = data.split("_")[2];
            MainCategory mainCategory = MainCategory.valueOf(categoryName);
            showSubCategories(chatId, mainCategory);
        } else if (data.startsWith("sub_category_")) {
            String subCategoryName = data.split("_")[2];
            SubCategory subCategory = SubCategory.valueOf(subCategoryName);
            showProductsBySubCategory(chatId, subCategory);
        } else if (data.startsWith("add_to_cart_")) {
            int productId = Integer.parseInt(data.split("_")[3]);
            addToCart(chatId, productId);
            sendMessage(chatId, "✅ Mahsulot savatga qo'shildi!");
        } else if (data.startsWith("back_to_sub_category_")) {
            String subCategoryName = data.split("_")[4];
            SubCategory subCategory = SubCategory.valueOf(subCategoryName);
            showProductsBySubCategory(chatId, subCategory);
        } else if (data.equals("back_to_main_categories")) {
            showCategories(chatId);
        }
    }

    @SneakyThrows
    private void sendWelcomeMessage(long chatId) {
        String welcomeText = "🎉 *Premium Parfyumeriya Do'konimizga Xush Kelibsiz!* 🎉\n\n" +
                "Bizda jahonning eng mashhur va original atirlar mavjud!\n\n" +
                "🛍️ Mahsulotlarimizni ko'rish\n" +
                "🛒 Savatchangizni tekshirish\n" +
                "📞 Biz bilan bog'lanish\n\n" +
                "*Hoziroq o'zingiz uchun eng yaxshi atirni tanlang!*";

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(welcomeText);
        message.setParseMode("Markdown");
        message.setReplyMarkup(getMainKeyboard());

        execute(message);
    }

    @SneakyThrows
    private void showProducts(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("🛍️ *Bizning mahsulotlarimiz:*");
        message.setParseMode("Markdown");
        message.setReplyMarkup(getProductsKeyboard());

        execute(message);
    }

    @SneakyThrows
    private void showProductDetail(long chatId, int productId) {
        Product product = products.stream()
                .filter(p -> p.getId() == productId)
                .findFirst()
                .orElse(null);

        if (product == null) return;

        SendPhoto photo = new SendPhoto();
        photo.setChatId(chatId);
        photo.setPhoto(new InputFile(product.getImageUrl()));

        String caption = String.format(
                "*%s*\n\n" +
                        "💰 Narxi: *%,d so'm*\n\n" +
                        "📝 Ta'rif: %s\n\n" +
                        "Qo'shish uchun tugmani bosing 👇",
                product.getName(),
                product.getPrice(),
                product.getDescription()
        );

        photo.setCaption(caption);
        photo.setParseMode("Markdown");
        photo.setReplyMarkup(getProductDetailKeyboard(productId));

        execute(photo);
    }

    @SneakyThrows
    private void showCart(long chatId) {
        List<CartItem> cart = userCarts.getOrDefault(chatId, new ArrayList<>());

        if (cart.isEmpty()) {
            sendMessage(chatId, "🛒 Savatingiz bo'sh\n\nMahsulot qo'shish uchun 'Mahsulotlar' bo'limiga o'ting");
            return;
        }

        StringBuilder text = new StringBuilder("🛒 *Savatingiz:*\n\n");
        int total = 0;

        for (CartItem item : cart) {
            Product product = getProductById(item.getProductId());
            if (product != null) {
                int itemTotal = product.getPrice() * item.getQuantity();
                text.append(String.format("• %s\n  Miqdor: %d × %,d = *%,d so'm*\n\n",
                        product.getName(), item.getQuantity(), product.getPrice(), itemTotal));
                total += itemTotal;
            }
        }

        text.append(String.format("💰 *Jami: %,d so'm*", total));

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text.toString());
        message.setParseMode("Markdown");
        message.setReplyMarkup(getCartKeyboard());

        execute(message);
    }

    @SneakyThrows
    private void showContact(long chatId) {
        String contactText = "📞 *Biz bilan bog'laning:*\n\n" +
                "📱 Telefon: +998 99 999 99 99\n" +
                "📱 Telegram: @optom_parfum\n" +
                "📧 Email: info@optomparfum.uz\n" +
                "🏢 Manzil: Toshkent sh., Yakkasaroy tumani\n\n" +
                "🕒 Ish vaqti: 9:00 - 18:00 (Dam olish - Yakshanba)\n\n" +
                "💫 Optom xaridlar uchun alohida chegirmalar mavjud!";

        sendMessage(chatId, contactText);
    }

    @SneakyThrows
    private void showInfo(long chatId) {
        String infoText = "ℹ️ *Do'kon haqida ma'lumot:*\n\n" +
                "🎯 Bizning maqsadimiz - sizga premium parfyumeriya mahsulotlarini taqdim etish!\n\n" +
                "✅ 100% Original mahsulotlar\n" +
                "✅ Qulay narxlar\n" +
                "✅ Tez yetkazib berish\n" +
                "✅ Professional maslahat\n\n" +
                "🚚 *Yetkazib berish:*\n" +
                "• Beshariq bo'ylab - 15,000 so'm\n" +
                "• Viloyatlarga - 40,000 so'm\n" +
                "• 1,000,000 so'mdan yuqori xaridlarda - BEPUL!\n\n" +
                "💳 *To'lov usullari:*\n" +
                "• Naqd pul\n" +
                "• Plastik karta\n" +
                "• Click, Payme\n" +
                "• Bank o'tkazmasi";

        sendMessage(chatId, infoText);
    }

    private void addToCart(long chatId, int productId) {
        List<CartItem> cart = userCarts.computeIfAbsent(chatId, k -> new ArrayList<>());

        CartItem existingItem = cart.stream()
                .filter(item -> item.getProductId() == productId)
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
        } else {
            cart.add(new CartItem(productId, 1));
        }

        Product product = getProductById(productId);
        sendMessage(chatId, "✅ " + product.getName() + " savatga qo'shildi!");
    }


    private void clearCart(long chatId) {
        userCarts.remove(chatId);
        sendMessage(chatId, "🗑️ Savat tozalandi");
    }

    @SneakyThrows
    private void checkout(long chatId) {
        List<CartItem> cart = userCarts.get(chatId);
        if (cart == null || cart.isEmpty()) {
            sendMessage(chatId, "Savatingiz bo'sh!");
            return;
        }

        int total = cart.stream()
                .mapToInt(item -> {
                    Product product = getProductById(item.getProductId());
                    return product != null ? product.getPrice() * item.getQuantity() : 0;
                })
                .sum();

        String orderText = "✅ *Buyurtmangiz qabul qilindi!*\n\n" +
                String.format("💰 Jami summa: *%,d so'm*\n\n", total) +
                "📞 Tez orada operatorlarimiz siz bilan bog'lanishadi.\n\n" +
                "🚚 Yetkazib berish 1-2 kun ichida amalga oshiriladi.\n\n" +
                "*Xarid uchun rahmat!* 🙏";

        clearCart(chatId);
        sendMessage(chatId, orderText);
    }

    private Product getProductById(int id) {
        return products.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @SneakyThrows
    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setParseMode("Markdown");
        execute(message);
    }

    private ReplyKeyboardMarkup getMainKeyboard() {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true);

        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("🛍️ Mahsulotlar"));
        row1.add(new KeyboardButton("🛒 Savat"));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("📞 Aloqa"));
        row2.add(new KeyboardButton("ℹ️ Ma'lumot"));

        rows.add(row1);
        rows.add(row2);

        keyboard.setKeyboard(rows);
        return keyboard;
    }

    private InlineKeyboardMarkup getProductsKeyboard() {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (Product product : products) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(product.getName() + " - " + String.format("%,d so'm", product.getPrice()));
            button.setCallbackData("product_" + product.getId());
            row.add(button);
            rows.add(row);
        }

        keyboard.setKeyboard(rows);
        return keyboard;
    }

    private InlineKeyboardMarkup getProductDetailKeyboard(int productId) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton addButton = new InlineKeyboardButton();
        addButton.setText("🛒 Savatga qo'shish");
        addButton.setCallbackData("add_" + productId);
        row1.add(addButton);
        rows.add(row1);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("⬅️ Orqaga");
        backButton.setCallbackData("back_to_products");
        row2.add(backButton);
        rows.add(row2);

        keyboard.setKeyboard(rows);
        return keyboard;
    }

    private InlineKeyboardMarkup getCartKeyboard() {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton checkoutButton = new InlineKeyboardButton();
        checkoutButton.setText("✅ Buyurtma berish");
        checkoutButton.setCallbackData("checkout");
        row1.add(checkoutButton);
        rows.add(row1);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton clearButton = new InlineKeyboardButton();
        clearButton.setText("🗑️ Savatni tozalash");
        clearButton.setCallbackData("clear_cart");
        row2.add(clearButton);
        rows.add(row2);

        keyboard.setKeyboard(rows);
        return keyboard;
    }

    private InlineKeyboardMarkup getMainCategoriesKeyboard() {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (MainCategory category : MainCategory.values()) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(category.getDisplayName());
            button.setCallbackData("main_category_" + category.name());
            row.add(button);
            rows.add(row);
        }

        keyboard.setKeyboard(rows);
        return keyboard;
    }

    private InlineKeyboardMarkup getSubCategoriesKeyboard(MainCategory mainCategory) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        // Filter subcategories by main category
        Arrays.stream(SubCategory.values())
                .filter(sub -> sub.getMainCategory() == mainCategory)
                .forEach(subCategory -> {
                    List<InlineKeyboardButton> row = new ArrayList<>();
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(subCategory.getDisplayName());
                    button.setCallbackData("sub_category_" + subCategory.name());
                    row.add(button);
                    rows.add(row);
                });

        // Add back button
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("⬅️ Orqaga");
        backButton.setCallbackData("back_to_main_categories");
        backRow.add(backButton);
        rows.add(backRow);

        keyboard.setKeyboard(rows);
        return keyboard;
    }

    // Kategoriyalarni ko'rsatish metodi
    @SneakyThrows
    private void showCategories(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("🛍️ *Asosiy kategoriyalardan birini tanlang:*");
        message.setParseMode("Markdown");
        message.setReplyMarkup(getMainCategoriesKeyboard());
        execute(message);
    }

    // Subkategoriyalarni ko'rsatish metodi
    @SneakyThrows
    private void showSubCategories(long chatId, MainCategory mainCategory) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Quyidagi subkategoriyalardan birini tanlang:");
        message.setParseMode("Markdown");
        message.setReplyMarkup(getSubCategoriesKeyboard(mainCategory));
        execute(message);
    }

    // Subkategoriya bo'yicha mahsulotlarni ko'rsatish metodi
    @SneakyThrows
    private void showProductsBySubCategory(long chatId, SubCategory subCategory) {
        List<Product> filteredProducts = products.stream()
                .filter(p -> p.getSubCategory() == subCategory)
                .collect(Collectors.toList());

        if (filteredProducts.isEmpty()) {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("❌ Bu kategoriyada hozircha mahsulotlar mavjud emas.");
            message.setReplyMarkup(getSubCategoriesKeyboard(subCategory.getMainCategory()));
            execute(message);
            return;
        }

        for (Product product : filteredProducts) {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId);
            sendPhoto.setPhoto(new InputFile(product.getImageUrl()));

            String caption = String.format("*%s*\n\n%s\n\n💰 Narxi: %,d so'm\n\n",
                    product.getName(), product.getDescription(), product.getPrice());

            sendPhoto.setCaption(caption);
            sendPhoto.setParseMode("Markdown");

            InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rows = new ArrayList<>();

            List<InlineKeyboardButton> row1 = new ArrayList<>();
            InlineKeyboardButton addToCartButton = new InlineKeyboardButton();
            addToCartButton.setText("🛒 Savatga qo'shish");
            addToCartButton.setCallbackData("add_to_cart_" + product.getId());
            row1.add(addToCartButton);

            List<InlineKeyboardButton> row2 = new ArrayList<>();
            InlineKeyboardButton backButton = new InlineKeyboardButton();
            backButton.setText("⬅️ Orqaga");
            backButton.setCallbackData("back_to_sub_category_" + subCategory.name());
            row2.add(backButton);

            rows.add(row1);
            rows.add(row2);
            keyboard.setKeyboard(rows);

            sendPhoto.setReplyMarkup(keyboard);
            execute(sendPhoto);
        }
    }

    // Kategoriya bo'yicha mahsulotlarni ko'rsatish metodi
    // Ushbu metod olib tashlandi, chunki Category tipi aniqlanmagan va ishlatilmayapti.

    // Product sinfi
    public static class Product {
        private int id;
        private String name;
        private int price;
        private String imageUrl;
        private String description;
        private SubCategory subCategory;

        public Product(int id, String name, int price, String imageUrl, String description, SubCategory subCategory) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.imageUrl = imageUrl;
            this.description = description;
            this.subCategory = subCategory;
        }

        // Getters
        public int getId() { return id; }
        public String getName() { return name; }
        public int getPrice() { return price; }
        public String getImageUrl() { return imageUrl; }
        public String getDescription() { return description; }
        public SubCategory getSubCategory() { return subCategory; }
        public MainCategory getMainCategory() { return subCategory.getMainCategory(); }
    }

    // CartItem sinfi
    public static class CartItem {
        private int productId;
        private int quantity;

        public CartItem(int productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        // Getters and Setters
        public int getProductId() { return productId; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    // Add categories enum
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

        public String getDisplayName() {
            return displayName;
        }
    }

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

        public String getDisplayName() { return displayName; }
        public MainCategory getMainCategory() { return mainCategory; }
    }
}