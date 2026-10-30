package ru.aqa;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@Tag("selenide")
class GoodsSelenideTests {
    private static final String API_URL = TestConfig.apiUrl();
    private final List<Long> createdProductIds = new ArrayList<>();
    private String productName;

    @BeforeEach
    void configureBrowser() {
        Configuration.browser = "chrome";
        Configuration.timeout = TestConfig.elementsTimeoutMs();
        TestConfig.printSafeSettings();
        productName = "Selenide-" + UUID.randomUUID().toString().substring(0, 8);
    }

    @AfterEach
    void cleanUp() {
        for (Long id : createdProductIds) {
            given().auth().preemptive()
                    .basic(TestConfig.adminUsername(), TestConfig.adminPassword())
                    .delete(API_URL + "/goods/{id}", id);
        }
        createdProductIds.clear();
        Selenide.closeWebDriver();
    }

    @Test
    void addProductThroughAdminAndSeeItOnStorefront() {
        AdminProductsPage adminPage = new AdminLoginPage().open().loginAsConfiguredAdmin();
        adminPage.check().productFormIsVisible();
        adminPage.addProduct(productName, 80);
        adminPage.check().notificationContains("успешно добавлен");
        trackProductIdByName(productName);

        MainPage mainPage = adminPage.openStorefront();
        mainPage.check().productIsVisible(productName);
    }

    @Test
    void addProductToCartAndSeeItThere() {
        String name = TestConfig.starterProductName();
        MainPage mainPage = new MainPage().open();
        mainPage.check().productIsVisible(name);
        mainPage.addProductToCart(name);

        mainPage.openCart().check()
                .containsProduct(name)
                .totalEquals(TestConfig.starterProductPrice());
    }

    @Test
    void wrongCredentialsAreRejectedByAdmin() {
        AdminLoginPage loginPage = new AdminLoginPage().open();
        loginPage.check().usernameFieldIsVisible()
                .passwordFieldIsVisible()
                .loginButtonIsVisible();
        loginPage.enterCredentials("wrong-user", "wrong-password");
        loginPage.check().usernameFieldContains("wrong-user")
                .passwordFieldContains("wrong-password");
        loginPage.login();

        loginPage.check().usernameFieldIsVisible()
                .pageBodyContains("Please sign in");
    }

    @Test
    void cartRemainsAfterPageRefresh() {
        String name = TestConfig.starterProductName();
        MainPage mainPage = new MainPage().open();
        mainPage.addProductToCart(name).refresh();

        mainPage.openCart().check()
                .containsProduct(name)
                .totalEquals(TestConfig.starterProductPrice());
    }

    @Test
    void overThreeHundredShowsJavaScriptAlert() {
        String starterName = TestConfig.starterProductName();
        MainPage mainPage = new MainPage().open();
        mainPage.addProductToCart(starterName)
                .setProductQuantity("Стакан", 6)
                .addProductToCart("Стакан");
        CartPage cartPage = mainPage.openCart();
        int total = TestConfig.starterProductPrice() + 6 * 50;
        cartPage.check().totalEquals(total);
        cartPage.placeOrder();
        cartPage.check().orderAlertContains("превышает лимит 300 ₽");
        cartPage.acceptOrderAlert();
    }

    private void trackProductIdByName(String name) {
        List<Map<String, Object>> goods = given().auth().preemptive()
                .basic(TestConfig.adminUsername(), TestConfig.adminPassword())
                .get(API_URL + "/goods/list").then().statusCode(200)
                .extract().jsonPath().getList("goods");
        goods.stream()
                .filter(item -> name.equals(item.get("name")))
                .map(item -> Long.parseLong(String.valueOf(item.get("id"))))
                .findFirst()
                .ifPresent(createdProductIds::add);
    }
}
