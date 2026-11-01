package ru.aqa;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Tag("selenide")
@DisplayName("UI-тесты магазина на Selenide")
class GoodsSelenideTests {
    private final GoodsApi api = new GoodsApi();
    private final List<Long> createdProductIds = new ArrayList<>();
    private String productName;

    @BeforeEach
    void configureBrowser() {
        Configuration.browser = TestConfig.value("browser.name");
        Configuration.timeout = TestConfig.elementsTimeoutMs();
        AllureSelenideListener.register();
        TestConfig.printSafeSettings();
        productName = TestConfig.value("product.name.prefix") + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    @AfterEach
    @Step("Delete products created by the Selenide tests and close the browser")
    void cleanUp() {
        for (Long id : createdProductIds) {
            api.deleteProduct(id);
        }
        createdProductIds.clear();
        Selenide.closeWebDriver();
    }

    @Test
    void addProductThroughAdminAndSeeItOnStorefront() {
        AdminProductsPage adminPage = new AdminLoginPage().open().loginAsConfiguredAdmin();
        adminPage.check().productFormIsVisible();
        adminPage.addProduct(productName, TestConfig.integer("product.test.add.price"));
        adminPage.check().notificationContains(TestConfig.value("message.admin.add.success"));
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
        loginPage.enterCredentials(TestConfig.value("test.admin.wrong.username"), TestConfig.value("test.admin.wrong.password"));
        loginPage.check().usernameFieldContains(TestConfig.value("test.admin.wrong.username"))
                .passwordFieldContains(TestConfig.value("test.admin.wrong.password"));
        loginPage.login();

        loginPage.check().usernameFieldIsVisible()
                .pageBodyContains(TestConfig.value("message.admin.login.rejected"));
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
                .setProductQuantity(TestConfig.value("product.glass.name"), 6)
                .addProductToCart(TestConfig.value("product.glass.name"));
        CartPage cartPage = mainPage.openCart();
        int total = TestConfig.starterProductPrice() + 6 * TestConfig.integer("product.glass.price");
        cartPage.check().totalEquals(total);
        cartPage.placeOrder();
        cartPage.check().orderAlertContains(TestConfig.value("message.order.limit.exceeded"));
        cartPage.acceptOrderAlert();
    }

    @Step("Find product '{name}' id and register it for cleanup")
    private void trackProductIdByName(String name) {
        api.findProductIdByName(name).ifPresent(createdProductIds::add);
    }
}
