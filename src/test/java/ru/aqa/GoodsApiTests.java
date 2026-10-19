package ru.aqa;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

@Tag("api")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GoodsApiTests {
    private static final String BASE_URI = "http://localhost:8080";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "secret123";
    private static final long MISSING_ID = Long.MAX_VALUE;

    private final List<Long> createdIds = new ArrayList<>();

    private RequestSpecification authorizedRequest() {
        return given()
                .baseUri(BASE_URI)
                .auth().preemptive().basic(USERNAME, PASSWORD);
    }

    @BeforeEach
    void resetCreatedProducts() {
        createdIds.clear();
    }

    @AfterEach
    void removeCreatedProducts() {
        for (long id : createdIds) {
            given().spec(authorizedRequest())
                    .delete("/goods/{id}", id)
                    .then()
                    .statusCode(anyOf(is(200), is(404)));
        }
    }

    // Задача 1.1: given/when/then; список товаров пуст.
    @Test
    @Order(1)
    void getGoodsListWithGivenWhenThenReturnsEmptyGoods() {
        given()
                .baseUri(BASE_URI)
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .body("goods", empty());
    }

    // Задача 1.2: запрос строится через RequestSpecification.
    @Test
    @Order(2)
    void getGoodsListWithRequestSpecificationReturnsEmptyGoods() {
        RequestSpecification requestSpecification = given()
                .baseUri(BASE_URI)
                .auth().preemptive().basic(USERNAME, PASSWORD);

        given()
                .spec(requestSpecification)
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .body("goods", empty());
    }

    // Задача 1.3: товар проверяется встроенной проверкой REST Assured.
    @Test
    @Order(3)
    void createdProductAppearsInListUsingRestAssuredMatcher() {
        String name = uniqueName("rest-assured");
        createProduct(name);

        given()
                .spec(authorizedRequest())
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .body("goods.name", hasItem(name));
    }

    // Задача 1.4: товар проверяется через AssertJ.
    @Test
    @Order(4)
    void createdProductAppearsInListUsingAssertJ() {
        String name = uniqueName("assertj");
        createProduct(name);

        Response response = given()
                .spec(authorizedRequest())
                .when()
                .get("/goods/list");

        assertThat(response.statusCode())
                .as("GET /goods/list должен вернуть 200, тело: %s", response.asString())
                .isEqualTo(200);
        List<String> names = response.jsonPath().getList("goods.name", String.class);
        assertThat(names)
                .as("Список товаров должен содержать созданный товар '%s'", name)
                .contains(name);
    }

    // Задача 2: POST /goods/add — все штатные коды из Swagger: 200 и 400.
    @Test
    @Order(5)
    void addEndpointReturns200ForValidProductAnd400ForInvalidProduct() {
        createProduct(uniqueName("post-valid"));

        Response invalidResponse = given()
                .spec(authorizedRequest())
                .contentType(JSON)
                .body("{\"name\":\"\",\"price\":-1}")
                .post("/goods/add");

        if (invalidResponse.statusCode() == 200) {
            trackCreatedId(invalidResponse);
        }
        assertThat(invalidResponse.statusCode())
                .as("POST /goods/add с пустым именем и отрицательной ценой должен вернуть 400; тело: %s",
                        invalidResponse.asString())
                .isEqualTo(400);
    }

    // Задача 2: GET /goods/{id} — ответы 200 и 404.
    @Test
    @Order(6)
    void getByIdEndpointReturns200ForExistingProductAnd404ForMissingProduct() {
        long id = createProduct(uniqueName("get"));

        given().spec(authorizedRequest())
                .get("/goods/{id}", id)
                .then()
                .statusCode(200);

        given().spec(authorizedRequest())
                .get("/goods/{id}", MISSING_ID)
                .then()
                .statusCode(404);
    }

    // Задача 2: DELETE /goods/{id} — ответы 200 и 404.
    @Test
    @Order(7)
    void deleteEndpointReturns200ForExistingProductAnd404ForMissingProduct() {
        long id = createProduct(uniqueName("delete"));

        given().spec(authorizedRequest())
                .delete("/goods/{id}", id)
                .then()
                .statusCode(200);
        createdIds.remove(id);

        given().spec(authorizedRequest())
                .delete("/goods/{id}", id)
                .then()
                .statusCode(404);
    }

    // Задача 2: PATCH /goods/{id} — ответы 200, 400 и 404.
    @Test
    @Order(8)
    void patchEndpointReturns200ForUpdate400ForDuplicateNameAnd404ForMissingProduct() {
        long firstId = createProduct(uniqueName("patch-first"));
        long secondId = createProduct(uniqueName("patch-second"));
        String updatedName = uniqueName("patch-updated");

        given().spec(authorizedRequest())
                .contentType(JSON)
                .body(productJson(updatedName, 12.5))
                .patch("/goods/{id}", firstId)
                .then()
                .statusCode(200);

        Response duplicateNameResponse = given()
                .spec(authorizedRequest())
                .contentType(JSON)
                .body(productJson(updatedName, 12.5))
                .patch("/goods/{id}", secondId);
        assertThat(duplicateNameResponse.statusCode())
                .as("PATCH /goods/{id} с уже занятым именем должен вернуть 400; тело: %s",
                        duplicateNameResponse.asString())
                .isEqualTo(400);

        given().spec(authorizedRequest())
                .contentType(JSON)
                .body(productJson(uniqueName("patch-missing"), 12.5))
                .patch("/goods/{id}", MISSING_ID)
                .then()
                .statusCode(404);
    }

    // Задача 2: GET /goods/list — штатный код ответа 200.
    @Test
    @Order(9)
    void listEndpointReturns200() {
        given().spec(authorizedRequest())
                .get("/goods/list")
                .then()
                .statusCode(200);
    }

    private long createProduct(String name) {
        Response response = given()
                .spec(authorizedRequest())
                .contentType(JSON)
                .body(productJson(name, 12.5))
                .post("/goods/add");

        assertThat(response.statusCode())
                .as("POST /goods/add должен вернуть 200 для товара '%s'; тело: %s", name, response.asString())
                .isEqualTo(200);
        return trackCreatedId(response);
    }

    private long trackCreatedId(Response response) {
        Map<String, Object> data = response.jsonPath().getMap("data");
        assertThat(data)
                .as("Ответ POST /goods/add должен содержать непустой объект data; тело: %s", response.asString())
                .isNotNull()
                .isNotEmpty();

        Object idValue = data.values().iterator().next();
        long id = Long.parseLong(String.valueOf(idValue));
        createdIds.add(id);
        return id;
    }

    private String uniqueName(String prefix) {
        return "hw4-" + prefix + "-" + UUID.randomUUID();
    }

    private String productJson(String name, double price) {
        return "{\"name\":\"" + name + "\",\"price\":" + price + "}";
    }
}
