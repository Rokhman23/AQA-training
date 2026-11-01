package ru.aqa;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.http.ContentType.JSON;

/** Common HTTP operations. Endpoint paths and endpoint-specific rules stay in subclasses. */
public abstract class BasicApi {
    private final RestApiBuilder builder;

    protected BasicApi() {
        this(new RestApiBuilder());
    }

    protected BasicApi(RestApiBuilder builder) {
        this.builder = builder;
    }

    @Step("Send GET request to {path}")
    protected Response get(String path, Object... pathParameters) {
        return builder.build().get(path, pathParameters);
    }

    @Step("Send POST request to {path}")
    protected Response post(String path, Object body) {
        return builder.build().contentType(JSON).body(body).post(path);
    }

    @Step("Send PATCH request to {path}")
    protected Response patch(String path, Object body, Object... pathParameters) {
        return builder.build().contentType(JSON).body(body).patch(path, pathParameters);
    }

    @Step("Send DELETE request to {path}")
    protected Response delete(String path, Object... pathParameters) {
        return builder.build().delete(path, pathParameters);
    }
}
