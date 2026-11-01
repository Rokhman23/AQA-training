package ru.aqa;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("api")
@DisplayName("Goods API endpoint tests")
class GoodsApiTests {
    private final GoodsApi api = new GoodsApi();
    private final ApiAssert check = new ApiAssert();
    private final List<Long> createdIds = new ArrayList<>();

    @BeforeEach
    void printTestConfiguration() {
        TestConfig.printSafeSettings();
    }

    @AfterEach
    @Step("Delete products created by API tests")
    void removeCreatedProducts() {
        for (long id : createdIds) {
            check.statusIsOneOf(api.deleteProduct(id), 200, 404);
        }
        createdIds.clear();
    }

    @Test
    @Tag("smoke")
    @DisplayName("GET goods list returns HTTP 200")
    void listEndpointReturns200() {
        check.statusIs(api.getGoodsList(), 200);
    }

    @Test
    @DisplayName("POST goods add accepts a valid product and rejects invalid data")
    void addEndpointAcceptsValidAndRejectsInvalidProduct() {
        createProduct(uniqueName());
        check.statusIs(api.addInvalidProduct(), 400);
    }

    @Test
    @DisplayName("GET goods by id returns existing product and 404 for a missing id")
    void getByIdEndpointReturnsExistingProductAnd404ForMissingProduct() {
        long id = createProduct(uniqueName());
        check.statusIs(api.getProduct(id), 200);
        check.statusIs(api.getProduct(TestConfig.longValue("api.test.missing.id")), 404);
    }

    @Test
    @DisplayName("DELETE goods removes an existing product")
    void deleteEndpointRemovesExistingProduct() {
        long id = createProduct(uniqueName());
        check.statusIs(api.deleteProduct(id), 200);
        createdIds.remove(id);
        check.statusIs(api.deleteProduct(id), 404);
    }

    @Test
    @DisplayName("PATCH goods updates a product and handles duplicate and missing products")
    void patchEndpointUpdatesProductAndHandlesInvalidRequests() {
        long firstId = createProduct(uniqueName());
        long secondId = createProduct(uniqueName());
        String updatedName = uniqueName();

        check.statusIs(api.updateProduct(firstId, updatedName, testPrice()), 200);
        check.statusIs(api.updateProduct(secondId, updatedName, testPrice()), 400);
        check.statusIs(api.updateProduct(TestConfig.longValue("api.test.missing.id"),
                uniqueName(), testPrice()), 404);
    }

    @Step("Create a product and register it for cleanup")
    private long createProduct(String name) {
        long id = api.createProduct(name, testPrice());
        assertTrue(id > 0, "The API should return a positive product id");
        createdIds.add(id);
        return id;
    }

    @Step("Generate a unique product name")
    private String uniqueName() {
        return TestConfig.value("product.name.prefix") + "-API-" + UUID.randomUUID();
    }

    @Step("Read configured API test price")
    private double testPrice() {
        return TestConfig.decimal("product.test.api.price");
    }
}
