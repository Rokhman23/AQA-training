package ru.aqa;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/** All API response checks, grouped as named Allure steps. */
public class GoodsApiAssert {
    @Step("Verify response status is {expectedStatus}")
    public GoodsApiAssert statusIs(Response response, int expectedStatus) {
        assertThat(response.statusCode())
                .as("Response body: %s", response.asString())
                .isEqualTo(expectedStatus);
        return this;
    }

    @Step("Verify response status is {firstStatus} or {secondStatus}")
    public GoodsApiAssert statusIsOneOf(Response response, int firstStatus, int secondStatus) {
        assertThat(response.statusCode())
                .as("Response body: %s", response.asString())
                .isIn(firstStatus, secondStatus);
        return this;
    }

    @Step("Verify that the goods list is empty")
    public GoodsApiAssert goodsListIsEmpty(Response response) {
        assertThat(response.jsonPath().getList("goods")).isEmpty();
        return this;
    }

    @Step("Verify that the goods list contains product '{expectedName}'")
    public GoodsApiAssert goodsListContains(Response response, String expectedName) {
        List<String> names = response.jsonPath().getList("goods.name", String.class);
        assertThat(names).contains(expectedName);
        return this;
    }

    @Step("Verify that the add-product response contains an id and return it")
    public long createdProductId(Response response) {
        statusIs(response, 200);
        Map<String, Object> data = response.jsonPath().getMap("data");
        assertThat(data)
                .as("Response body: %s", response.asString())
                .isNotNull()
                .isNotEmpty();
        Object id = data.values().iterator().next();
        return Long.parseLong(String.valueOf(id));
    }
}
