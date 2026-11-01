package ru.aqa;

import org.assertj.core.api.AbstractAssert;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static org.assertj.core.api.Assertions.assertThat;

/** Assertions for the admin sign-in form. */
public class AdminLoginPageAssert extends AbstractAssert<AdminLoginPageAssert, AdminLoginPage> {
    public AdminLoginPageAssert(AdminLoginPage actual) {
        super(actual, AdminLoginPageAssert.class);
    }

    @Step("Check that username field is visible")
    public AdminLoginPageAssert usernameFieldIsVisible() {
        actual.usernameField.shouldBe(visible);
        return this;
    }

    @Step("Check that password field is visible")
    public AdminLoginPageAssert passwordFieldIsVisible() {
        actual.passwordField.shouldBe(visible);
        return this;
    }

    @Step("Check that login button is visible")
    public AdminLoginPageAssert loginButtonIsVisible() {
        actual.loginButton.shouldBe(visible);
        return this;
    }

    @Step("Check that username field contains the expected value")
    public AdminLoginPageAssert usernameFieldContains(String value) {
        assertThat(actual.usernameField.getValue()).isEqualTo(value);
        return this;
    }

    @Step("Check that password field contains the expected value")
    public AdminLoginPageAssert passwordFieldContains(String value) {
        assertThat(actual.passwordField.getValue()).isEqualTo(value);
        return this;
    }

    @Step("Check that page contains '{value}'")
    public AdminLoginPageAssert pageBodyContains(String value) {
        actual.pageBody.shouldHave(com.codeborne.selenide.Condition.text(value));
        return this;
    }
}
