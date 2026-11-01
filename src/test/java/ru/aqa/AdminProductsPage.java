package ru.aqa;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

/** Page object for product management in the admin panel. */
public class AdminProductsPage {
    final SelenideElement nameField = $("#n-name");
    final SelenideElement priceField = $("#n-price");
    final SelenideElement addButton = $("#add-btn");
    final SelenideElement toastContainer = $("#toast-container");
    final SelenideElement productsTable = $("#tbody");

    public AdminProductsPageAssert check() {
        return new AdminProductsPageAssert(this);
    }

    @Step("Add product '{name}' with price {price}")
    public AdminProductsPage addProduct(String name, int price) {
        nameField.setValue(name);
        priceField.setValue(String.valueOf(price));
        addButton.click();
        return this;
    }

    @Step("Edit product {id}: set name '{name}' and price {price}")
    public AdminProductsPage editProduct(long id, String name, int price) {
        $("#nm-" + id).setValue(name);
        $("#pr-" + id).setValue(String.valueOf(price));
        $("#tbody [data-action='update'][data-id='" + id + "']").click();
        return this;
    }

    @Step("Open storefront from the admin page")
    public MainPage openStorefront() {
        Selenide.open(TestConfig.uiUrl());
        return new MainPage();
    }
}
