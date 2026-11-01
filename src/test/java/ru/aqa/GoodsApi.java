package ru.aqa;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;
import java.util.Map;
import java.util.OptionalLong;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

/** API operations used by API and UI tests. All requests carry AllureRestAssured. */
public class GoodsApi {
    private final GoodsApiAssert check = new GoodsApiAssert();

    @Step("Prepare authorized API request")
    private RequestSpecification authorizedRequest() {
        return given()
                .baseUri(TestConfig.apiUrl())
                .auth().preemptive().basic(TestConfig.adminUsername(), TestConfig.adminPassword())
                .filter(new AllureRestAssured());
    }

    @Step("GET /goods/list")
    public Response getGoodsList() {
        return given().spec(authorizedRequest()).get("/goods/list");
    }

    @Step("GET /goods/list using a RequestSpecification")
    public Response getGoodsListWithRequestSpecification() {
        RequestSpecification specification = authorizedRequest();
        return given().spec(specification).get("/goods/list");
    }

    @Step("POST /goods/add: create product '{name}' for {price}")
    public Response addProduct(String name, double price) {
        return given().spec(authorizedRequest())
                .contentType(JSON)
                .body(productJson(name, price))
                .post("/goods/add");
    }

    @Step("POST /goods/add: send invalid product")
    public Response addInvalidProduct() {
        return given().spec(authorizedRequest())
                .contentType(JSON)
                .body("{\"name\":\"\",\"price\":-1}")
                .post("/goods/add");
    }

    @Step("GET /goods/{id}: retrieve product {id}")
    public Response getProduct(long id) {
        return given().spec(authorizedRequest()).get("/goods/{id}", id);
    }

    @Step("DELETE /goods/{id}: delete product {id}")
    public Response deleteProduct(long id) {
        return given().spec(authorizedRequest()).delete("/goods/{id}", id);
    }

    @Step("PATCH /goods/{id}: update product {id}")
    public Response updateProduct(long id, String name, double price) {
        return given().spec(authorizedRequest())
                .contentType(JSON)
                .body(productJson(name, price))
                .patch("/goods/{id}", id);
    }

    @Step("Create product '{name}' through the API and return its id")
    public long createProduct(String name, double price) {
        Response response = addProduct(name, price);
        return check.createdProductId(response);
    }

    @Step("Find product id by name '{name}' using GET /goods/list")
    public OptionalLong findProductIdByName(String name) {
        Response response = getGoodsList();
        check.statusIs(response, 200);
        List<Map<String, Object>> goods = response.jsonPath().getList("goods");
        if (goods == null) {
            return OptionalLong.empty();
        }
        return goods.stream()
                .filter(item -> name.equals(item.get("name")))
                .mapToLong(item -> Long.parseLong(String.valueOf(item.get("id"))))
                .findFirst();
    }

    @Step("Serialize product '{name}' for the API")
    private String productJson(String name, double price) {
        return "{\"name\":\"" + name + "\",\"price\":" + price + "}";
    }
}
