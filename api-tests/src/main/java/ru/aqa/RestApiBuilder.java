package ru.aqa;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/** Builds the shared, authenticated REST Assured request used by endpoint clients. */
public class RestApiBuilder {
    @Step("Build common API request")
    public RequestSpecification build() {
        return given()
                .baseUri(TestConfig.apiUrl())
                .auth().preemptive().basic(TestConfig.adminUsername(), TestConfig.adminPassword())
                .filter(new AllureRestAssured());
    }
}
