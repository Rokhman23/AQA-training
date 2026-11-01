package ru.aqa;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Tag("api")
@DisplayName("Проверки API каталога товаров")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GoodsApiTests {
    private static final double TEST_PRICE = 12.5;
    private static final long MISSING_ID = Long.MAX_VALUE;

    private final GoodsApi api = new GoodsApi();
    private final GoodsApiAssert check = new GoodsApiAssert();
    private final List<Long> createdIds = new ArrayList<>();

    @BeforeEach
    void printTestConfiguration() {
        TestConfig.printSafeSettings();
    }

    @AfterEach
    @Step("Delete products created by this API test")
    void removeCreatedProducts() {
        for (long id : createdIds) {
            check.statusIsOneOf(api.deleteProduct(id), 200, 404);
        }
        createdIds.clear();
    }

    // Задача 1.1: given/when/then; список товаров пуст.
    @Test
    @Order(1)
    void getGoodsListWithGivenWhenThenReturnsEmptyGoods() {
        Response response = api.getGoodsList();
        check.statusIs(response, 200).goodsListIsEmpty(response);
    }

    // Задача 1.2: запрос строится через RequestSpecification.
    @Test
    @Order(2)
    void getGoodsListWithRequestSpecificationReturnsEmptyGoods() {
        Response response = api.getGoodsListWithRequestSpecification();
        check.statusIs(response, 200).goodsListIsEmpty(response);
    }

    // Задача 1.3: товар проверяется встроенной проверкой REST Assured.
    @Test
    @Order(3)
    void createdProductAppearsInListUsingRestAssuredMatcher() {
        String name = uniqueName("rest-assured");
        createProduct(name);
        Response response = api.getGoodsList();
        check.statusIs(response, 200).goodsListContains(response, name);
    }

    // Задача 1.4: товар проверяется через AssertJ.
    @Test
    @Order(4)
    void createdProductAppearsInListUsingAssertJ() {
        String name = uniqueName("assertj");
        createProduct(name);
        Response response = api.getGoodsList();
        check.statusIs(response, 200).goodsListContains(response, name);
    }

    // Задача 2: POST /goods/add — ответы 200 и 400.
    @Test
    @Order(5)
    void addEndpointReturns200ForValidProductAnd400ForInvalidProduct() {
        createProduct(uniqueName("post-valid"));
        check.statusIs(api.addInvalidProduct(), 400);
    }

    // Задача 2: GET /goods/{id} — ответы 200 и 404.
    @Test
    @Order(6)
    void getByIdEndpointReturns200ForExistingProductAnd404ForMissingProduct() {
        long id = createProduct(uniqueName("get"));
        check.statusIs(api.getProduct(id), 200);
        check.statusIs(api.getProduct(MISSING_ID), 404);
    }

    // Задача 2: DELETE /goods/{id} — ответы 200 и 404.
    @Test
    @Order(7)
    void deleteEndpointReturns200ForExistingProductAnd404ForMissingProduct() {
        long id = createProduct(uniqueName("delete"));
        check.statusIs(api.deleteProduct(id), 200);
        createdIds.remove(id);
        check.statusIs(api.deleteProduct(id), 404);
    }

    // Задача 2: PATCH /goods/{id} — ответы 200, 400 и 404.
    @Test
    @Order(8)
    void patchEndpointReturns200ForUpdate400ForDuplicateNameAnd404ForMissingProduct() {
        long firstId = createProduct(uniqueName("patch-first"));
        long secondId = createProduct(uniqueName("patch-second"));
        String updatedName = uniqueName("patch-updated");

        check.statusIs(api.updateProduct(firstId, updatedName, TEST_PRICE), 200);
        check.statusIs(api.updateProduct(secondId, updatedName, TEST_PRICE), 400);
        check.statusIs(api.updateProduct(MISSING_ID, uniqueName("patch-missing"), TEST_PRICE), 404);
    }

    // Задача 2: GET /goods/list — штатный код ответа 200.
    @Test
    @Order(9)
    void listEndpointReturns200() {
        check.statusIs(api.getGoodsList(), 200);
    }

    @Step("Create product '{name}' and register it for cleanup")
    private long createProduct(String name) {
        long id = api.createProduct(name, TEST_PRICE);
        createdIds.add(id);
        return id;
    }

    @Step("Generate a unique product name with prefix '{prefix}'")
    private String uniqueName(String prefix) {
        return "hw4-" + prefix + "-" + UUID.randomUUID();
    }
}
