package ru.aqa;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;

@Tag("webinar")
class GoodsWebinarSelenideTests {
    private static final String BASE_URL = GoodsSelenideTests.BASE_URL;
    private static final String USER = GoodsSelenideTests.USER;
    private static final String PASSWORD = GoodsSelenideTests.PASSWORD;
    private final List<Long> createdIds = new ArrayList<>();

    @AfterEach
    void removeApiGeneratedProducts() {
        for (Long id : createdIds) {
            given().auth().preemptive().basic(USER, PASSWORD)
                    .delete(BASE_URL + "/goods/{id}", id);
        }
        createdIds.clear();
        closeWebDriver();
    }

    @Test
    void orderThreeUnitsAndSeeProcessingNotification() {
        open(BASE_URL + "/");
        var product = $$(".product-card").findBy(text("Учебный товар 25"));
        product.$("input").setValue("3");
        product.$("[data-action='add-to-cart']").click();
        $("#open-cart-btn").click();
        $("#total-price").shouldHave(text("75"));
        $("#makeOrder").click();
        $("#toast-container").shouldHave(text("Заказ принят в обработку"));
    }

    @Test
    void cartTotalIsCorrectForSeveralDifferentProducts() {
        String firstName = uniqueName("api-first");
        String secondName = uniqueName("api-second");
        createProduct(firstName, 45);
        createProduct(secondName, 80);
        open(BASE_URL + "/");
        $$(".product-card").findBy(text(firstName)).$("[data-action='add-to-cart']").click();
        $$(".product-card").findBy(text(secondName)).$("[data-action='add-to-cart']").click();
        $("#open-cart-btn").click();
        $("#cart-items").shouldHave(text(firstName)).shouldHave(text(secondName));
        $("#total-price").shouldHave(text("125"));
    }

    @Test
    void addProductInAdminAndSeeSuccessNotification() {
        String name = uniqueName("ui-added");
        openAdmin();
        $("#n-name").setValue(name);
        $("#n-price").setValue("62");
        $("#add-btn").click();
        $("#toast-container").shouldHave(text("Товар успешно добавлен"));
        trackIdByName(name);
    }

    @Test
    void editProductInAdminAndSeeUpdatedValueOnStorefront() {
        String oldName = uniqueName("before-edit");
        String newName = uniqueName("after-edit");
        long id = createProduct(oldName, 40);
        openAdmin();
        $("#nm-" + id).setValue(newName);
        $("#pr-" + id).setValue("55");
        $("#tbody [data-action='update'][data-id='" + id + "']").click();
        $("#toast-container").shouldHave(text("обновлен"));
        open(BASE_URL + "/");
        $("#products-list").shouldHave(text(newName));
        $$(".product-card").findBy(text(newName)).shouldHave(text("55"));
    }

    private long createProduct(String name, double price) {
        Response response = given().auth().preemptive().basic(USER, PASSWORD)
                .contentType("application/json")
                .body("{\"name\":\"" + name + "\",\"price\":" + price + "}")
                .post(BASE_URL + "/goods/add");
        response.then().statusCode(200);
        Map<String, Object> data = response.jsonPath().getMap("data");
        Object rawId = data.values().iterator().next();
        long id = Long.parseLong(String.valueOf(rawId));
        createdIds.add(id);
        return id;
    }

    private void trackIdByName(String name) {
        List<Map<String, Object>> goods = given().auth().preemptive().basic(USER, PASSWORD)
                .get(BASE_URL + "/goods/list").then().statusCode(200)
                .extract().jsonPath().getList("goods");
        goods.stream().filter(item -> name.equals(item.get("name")))
                .map(item -> Long.parseLong(String.valueOf(item.get("id"))))
                .findFirst().ifPresent(createdIds::add);
    }

    private void openAdmin() {
        open(BASE_URL + "/admin");
        $("#username").setValue(USER);
        $("#password").setValue(PASSWORD);
        $("button[type='submit']").click();
        $("#n-name").shouldBe(com.codeborne.selenide.Condition.visible);
    }

    private String uniqueName(String suffix) {
        return "HW6-" + suffix + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}




