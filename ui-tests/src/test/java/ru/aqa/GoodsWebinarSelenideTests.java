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


@Tag("webinar")
@DisplayName("Сценарии вебинара: работа с заказами и админкой")
class GoodsWebinarSelenideTests {
    private final GoodsApi api = new GoodsApi();
    private final ApiAssert apiCheck = new ApiAssert();
    private final List<Long> createdIds = new ArrayList<>();

    @BeforeEach
    void configureBrowser() {
        Configuration.browser = TestConfig.value("browser.name");
        Configuration.timeout = TestConfig.elementsTimeoutMs();
        AllureSelenideListener.register();
        TestConfig.printSafeSettings();
    }

    @AfterEach
    @Step("Delete products created by the webinar tests and close the browser")
    void removeCreatedProductsAndCloseBrowser() {
        for (Long id : createdIds) {
            apiCheck.statusIsOneOf(api.deleteProduct(id), 200, 404);
        }
        createdIds.clear();
        Selenide.closeWebDriver();
    }

    @Test
    @Tag("smoke")
    void orderThreeUnitsAndSeeProcessingNotification() {
        String productName = TestConfig.starterProductName();
        MainPage mainPage = new MainPage().open();
        mainPage.check().productIsVisible(productName);
        mainPage.setProductQuantity(productName, 3).addProductToCart(productName);

        CartPage cartPage = mainPage.openCart();
        cartPage.check()
                .containsProduct(productName)
                .totalEquals(3 * TestConfig.starterProductPrice())
                .totalIsAtMost(TestConfig.integer("order.total.limit"));
        cartPage.placeOrder();

        mainPage.check().notificationContains(TestConfig.value("message.order.accepted"));
    }

    @Test
    void cartTotalIsCorrectForSeveralDifferentProducts() {
        String firstName = uniqueName("first");
        String secondName = uniqueName("second");
        createProduct(firstName, TestConfig.integer("product.test.first.price"));
        createProduct(secondName, TestConfig.integer("product.test.second.price"));

        MainPage mainPage = new MainPage().open();
        mainPage.addProductToCart(firstName).addProductToCart(secondName);
        CartPage cartPage = mainPage.openCart();
        cartPage.check()
                .containsProduct(firstName)
                .containsProduct(secondName)
                .totalEquals(TestConfig.integer("product.test.first.price") + TestConfig.integer("product.test.second.price"));
    }

    @Test
    void addProductInAdminAndSeeSuccessNotification() {
        String name = uniqueName("added");
        AdminProductsPage adminPage = new AdminLoginPage().open().loginAsConfiguredAdmin();
        adminPage.check().productFormIsVisible();
        adminPage.addProduct(name, TestConfig.integer("product.test.admin.add.price"));
        adminPage.check().notificationContains(TestConfig.value("message.admin.add.success"));
        trackIdByName(name);
    }

    @Test
    void editProductInAdminAndSeeUpdatedValueOnStorefront() {
        String oldName = uniqueName("before-edit");
        String newName = uniqueName("after-edit");
        long id = createProduct(oldName, TestConfig.integer("product.test.admin.edit.initial.price"));

        AdminProductsPage adminPage = new AdminLoginPage().open().loginAsConfiguredAdmin();
        adminPage.check().productFormIsVisible();
        adminPage.editProduct(id, newName, TestConfig.integer("product.test.admin.edit.updated.price"));
        adminPage.check().notificationContains(TestConfig.value("message.admin.update.success"));

        MainPage mainPage = adminPage.openStorefront();
        mainPage.check().productContainsText(newName, String.valueOf(TestConfig.integer("product.test.admin.edit.updated.price")));
    }

    @Step("Create product '{name}' through the API and register it for cleanup")
    private long createProduct(String name, int price) {
        long id = api.createProduct(name, price);
        createdIds.add(id);
        return id;
    }

    @Step("Find product '{name}' id and register it for cleanup")
    private void trackIdByName(String name) {
        api.findProductIdByName(name).ifPresent(createdIds::add);
    }

    @Step("Generate a unique product name with suffix '{suffix}'")
    private String uniqueName(String suffix) {
        return TestConfig.value("product.name.prefix") + "-" + suffix + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
