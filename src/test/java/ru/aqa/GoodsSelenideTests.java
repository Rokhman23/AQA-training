package ru.aqa;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

@Tag("selenide")
class GoodsSelenideTests {
    static final String BASE_URL = TestConfig.uiUrl();
    static final String USER = TestConfig.adminUsername();
    static final String PASSWORD = TestConfig.adminPassword();
    static final String STARTER_PRODUCT_NAME = TestConfig.starterProductName();
    static final int STARTER_PRODUCT_PRICE = TestConfig.starterProductPrice();
    private String name;

    @BeforeEach
    void configureBrowser() {
        Configuration.browser = "chrome";
        Configuration.timeout = TestConfig.elementsTimeoutMs();
        TestConfig.printSafeSettings();
        name = "Selenide " + UUID.randomUUID().toString().substring(0, 8);
    }

    @AfterEach
    void closeBrowser() { Selenide.closeWebDriver(); }

    @Test
    void addProductThroughAdminAndSeeItOnStorefront() {
        openAdmin();
        $("#n-name").setValue(name);
        $("#n-price").setValue("80");
        $("#add-btn").click();
        $("#toast-container").shouldHave(text("успешно добавлен"));
        open(BASE_URL + "/");
        $("#products-list").shouldHave(text(name));
    }

    @Test
    void addProductToCartAndSeeItThere() {
        open(BASE_URL + "/");
        $$(".product-card").findBy(text(STARTER_PRODUCT_NAME))
                .$("[data-action='add-to-cart']").click();
        $("#open-cart-btn").click();
        $("#cart-items .cart-item").shouldHave(text(STARTER_PRODUCT_NAME));
        $("#total-price").shouldHave(text(String.valueOf(STARTER_PRODUCT_PRICE)));
    }

    @Test
    void wrongCredentialsAreRejectedByAdmin() {
        open(BASE_URL + "/admin");
        $("#username").setValue("wrong-user");
        $("#password").setValue("wrong-password");
        $("button[type='submit']").click();
        $("#username").shouldBe(visible);
        $("body").shouldHave(text("Please sign in"));
    }

    @Test
    void cartRemainsAfterPageRefresh() {
        open(BASE_URL + "/");
        $$(".product-card").findBy(text(STARTER_PRODUCT_NAME))
                .$("[data-action='add-to-cart']").click();
        refresh();
        $("#open-cart-btn").click();
        $("#cart-items").shouldHave(text(STARTER_PRODUCT_NAME));
    }

    @Test
    void overThreeHundredShowsJavaScriptAlert() {
        open(BASE_URL + "/");
        $$(".product-card").findBy(text(STARTER_PRODUCT_NAME))
                .$("[data-action='add-to-cart']").click();
        // Increase the quantity of the 50-ruble product to make the cart exceed 300.
        $$(".product-card").findBy(text("Стакан")).$("input").setValue("6");
        $$(".product-card").findBy(text("Стакан"))
                .$("[data-action='add-to-cart']").click();
        $("#open-cart-btn").click();
        $("#makeOrder").click();
        confirm("[SmartShop]: Денег не хватает! Сумма " + (300 + STARTER_PRODUCT_PRICE) + " ₽ превышает лимит 300 ₽.");
    }

    private void openAdmin() {
        open(BASE_URL + "/admin");
        $("#username").setValue(USER);
        $("#password").setValue(PASSWORD);
        $("button[type='submit']").click();
        $("#n-name").shouldBe(visible);
    }
}







