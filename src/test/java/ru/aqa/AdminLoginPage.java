package ru.aqa;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

/** Page object for the admin sign-in form. */
public class AdminLoginPage {
    final SelenideElement usernameField = $("#username");
    final SelenideElement passwordField = $("#password");
    final SelenideElement loginButton = $("button[type='submit']");
    final SelenideElement pageBody = $("body");

    public AdminLoginPageAssert check() {
        return new AdminLoginPageAssert(this);
    }

    public AdminLoginPage open() {
        Selenide.open(TestConfig.uiUrl() + "/admin");
        return this;
    }

    public AdminLoginPage enterUsername(String username) {
        usernameField.setValue(username);
        return this;
    }

    public AdminLoginPage enterPassword(String password) {
        passwordField.setValue(password);
        return this;
    }

    public AdminLoginPage enterCredentials(String username, String password) {
        return enterUsername(username).enterPassword(password);
    }

    public AdminProductsPage login() {
        loginButton.click();
        return new AdminProductsPage();
    }

    public AdminProductsPage loginAsConfiguredAdmin() {
        return enterCredentials(TestConfig.adminUsername(), TestConfig.adminPassword()).login();
    }
}
