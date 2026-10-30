package ru.aqa;

import io.restassured.response.Response;
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

@Tag("webinar")
class GoodsWebinarSelenideTests {
    private static final String API_URL = TestConfig.apiUrl();
    private final List<Long> createdIds = new ArrayList<>();

    @BeforeEach
    void configureBrowser() {
        Configuration.browser = "chrome";
        Configuration.timeout = TestConfig.elementsTimeoutMs();
        TestConfig.printSafeSettings();
    }

    @AfterEach
    void removeCreatedProductsAndCloseBrowser() {
        for (Long id : createdIds) {
            given().auth().preemptive()
                    .basic(TestConfig.adminUsername(), TestConfig.adminPassword())
                    .delete(API_URL + "/goods/{id}", id);
        }
        createdIds.clear();
        Selenide.closeWebDriver();
    }

    @Test
    void orderThreeUnitsAndSeeProcessingNotification() {
        String productName = TestConfig.starterProductName();
        MainPage mainPage = new MainPage().open();
        mainPage.check().productIsVisible(productName);
        mainPage.setProductQuantity(productName, 3).addProductToCart(productName);

        CartPage cartPage = mainPage.openCart();
        cartPage.check()
                .containsProduct(productName)
                .totalEquals(3 * TestConfig.starterProductPrice())
                .totalIsAtMost(300);
        cartPage.placeOrder();

        mainPage.check().notificationContains("Заказ принят в обработку");
    }

    @Test
    void cartTotalIsCorrectForSeveralDifferentProducts() {
        String firstName = uniqueName("first");
        String secondName = uniqueName("second");
        createProduct(firstName, 45);
        createProduct(secondName, 80);

        MainPage mainPage = new MainPage().open();
        mainPage.addProductToCart(firstName).addProductToCart(secondName);
        CartPage cartPage = mainPage.openCart();
        cartPage.check()
                .containsProduct(firstName)
                .containsProduct(secondName)
                .totalEquals(125);
    }

    @Test
    void addProductInAdminAndSeeSuccessNotification() {
        String name = uniqueName("added");
        AdminProductsPage adminPage = new AdminLoginPage().open().loginAsConfiguredAdmin();
        adminPage.check().productFormIsVisible();
        adminPage.addProduct(name, 62);
        adminPage.check().notificationContains("Товар успешно добавлен");
        trackIdByName(name);
    }

    @Test
    void editProductInAdminAndSeeUpdatedValueOnStorefront() {
        String oldName = uniqueName("before-edit");
        String newName = uniqueName("after-edit");
        long id = createProduct(oldName, 40);

        AdminProductsPage adminPage = new AdminLoginPage().open().loginAsConfiguredAdmin();
        adminPage.check().productFormIsVisible();
        adminPage.editProduct(id, newName, 55);
        adminPage.check().notificationContains("обновлен");

        MainPage mainPage = adminPage.openStorefront();
        mainPage.check().productContainsText(newName, "55");
    }

    private long createProduct(String name, int price) {
        String body = """
                {"name":"%s","price":%d}
                """.formatted(name, price);
        Response response = given().auth().preemptive()
                .basic(TestConfig.adminUsername(), TestConfig.adminPassword())
                .contentType("application/json")
                .body(body)
                .post(API_URL + "/goods/add");
        response.then().statusCode(200);

        Map<String, Object> data = response.jsonPath().getMap("data");
        Object rawId = data.values().iterator().next();
        long id = Long.parseLong(String.valueOf(rawId));
        createdIds.add(id);
        return id;
    }

    private void trackIdByName(String name) {
        List<Map<String, Object>> goods = given().auth().preemptive()
                .basic(TestConfig.adminUsername(), TestConfig.adminPassword())
                .get(API_URL + "/goods/list").then().statusCode(200)
                .extract().jsonPath().getList("goods");
        goods.stream()
                .filter(item -> name.equals(item.get("name")))
                .map(item -> Long.parseLong(String.valueOf(item.get("id"))))
                .findFirst()
                .ifPresent(createdIds::add);
    }

    private String uniqueName(String suffix) {
        return "HW9-" + suffix + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
