package ru.aqa;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;
import java.util.OptionalLong;

/** Endpoint-specific operations for the goods service. */
public class GoodsApi extends BasicApi {
    private final ApiAssert check = new ApiAssert();

    @Step("GET goods list")
    public Response getGoodsList() {
        return get(GoodsEndpoints.list());
    }

    @Step("GET goods list with a separate API request")
    public Response getGoodsListWithRequestSpecification() {
        return get(GoodsEndpoints.list());
    }

    @Step("POST a valid product named '{name}' with price {price}")
    public Response addProduct(String name, double price) {
        return post(GoodsEndpoints.add(), Map.of("name", name, "price", price));
    }

    @Step("POST an invalid product")
    public Response addInvalidProduct() {
        return post(GoodsEndpoints.add(), TestConfig.value("api.test.invalid.product.json"));
    }

    @Step("GET product with id {id}")
    public Response getProduct(long id) {
        return get(GoodsEndpoints.byId(), id);
    }

    @Step("DELETE product with id {id}")
    public Response deleteProduct(long id) {
        return delete(GoodsEndpoints.byId(), id);
    }

    @Step("PATCH product {id} with name '{name}' and price {price}")
    public Response updateProduct(long id, String name, double price) {
        return patch(GoodsEndpoints.byId(), Map.of("name", name, "price", price), id);
    }

    @Step("Create product '{name}' and return its id")
    public long createProduct(String name, double price) {
        return check.createdProductId(addProduct(name, price));
    }

    @Step("Find product id by name '{name}'")
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
}
