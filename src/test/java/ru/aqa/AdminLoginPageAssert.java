package ru.aqa;

import org.assertj.core.api.AbstractAssert;

import static com.codeborne.selenide.Condition.visible;
import static org.assertj.core.api.Assertions.assertThat;

/** Assertions for the admin sign-in form. */
public class AdminLoginPageAssert extends AbstractAssert<AdminLoginPageAssert, AdminLoginPage> {
    public AdminLoginPageAssert(AdminLoginPage actual) {
        super(actual, AdminLoginPageAssert.class);
    }

    public AdminLoginPageAssert usernameFieldIsVisible() {
        actual.usernameField.shouldBe(visible);
        return this;
    }

    public AdminLoginPageAssert passwordFieldIsVisible() {
        actual.passwordField.shouldBe(visible);
        return this;
    }

    public AdminLoginPageAssert loginButtonIsVisible() {
        actual.loginButton.shouldBe(visible);
        return this;
    }

    public AdminLoginPageAssert usernameFieldContains(String value) {
        assertThat(actual.usernameField.getValue()).isEqualTo(value);
        return this;
    }

    public AdminLoginPageAssert passwordFieldContains(String value) {
        assertThat(actual.passwordField.getValue()).isEqualTo(value);
        return this;
    }

    public AdminLoginPageAssert pageBodyContains(String value) {
        actual.pageBody.shouldHave(com.codeborne.selenide.Condition.text(value));
        return this;
    }
}
