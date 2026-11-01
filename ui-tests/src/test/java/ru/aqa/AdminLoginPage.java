package ru.aqa;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

/** Page object for the admin sign-in form. */
public class AdminLoginPage extends BasePage {
    final SelenideElement usernameField = $("#username");
    final SelenideElement passwordField = $("#password");
    final SelenideElement loginButton = $("button[type='submit']");
    final SelenideElement pageBody = $("body");

    public AdminLoginPageAssert check() {
        return new AdminLoginPageAssert(this);
    }

    @Step("Open admin login page")
    public AdminLoginPage open() {
        Selenide.open(TestConfig.uiUrl() + TestConfig.value("ui.admin.path"));
        return this;
    }

    @Step("Enter username")
    public AdminLoginPage enterUsername(String username) {
        usernameField.setValue(username);
        return this;
    }

    @Step("Enter password")
    public AdminLoginPage enterPassword(String password) {
        passwordField.setValue(password);
        return this;
    }

    @Step("Enter admin credentials")
    public AdminLoginPage enterCredentials(String username, String password) {
        return enterUsername(username).enterPassword(password);
    }

    @Step("Submit admin login form")
    public AdminProductsPage login() {
        loginButton.click();
        return new AdminProductsPage();
    }

    @Step("Log in with configured admin credentials")
    public AdminProductsPage loginAsConfiguredAdmin() {
        return enterCredentials(TestConfig.adminUsername(), TestConfig.adminPassword()).login();
    }
}
