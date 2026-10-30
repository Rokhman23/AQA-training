package ru.aqa;

import org.assertj.core.api.AbstractAssert;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

/** Assertions for admin product management. */
public class AdminProductsPageAssert extends AbstractAssert<AdminProductsPageAssert, AdminProductsPage> {
    public AdminProductsPageAssert(AdminProductsPage actual) {
        super(actual, AdminProductsPageAssert.class);
    }

    public AdminProductsPageAssert productFormIsVisible() {
        actual.nameField.shouldBe(visible);
        actual.priceField.shouldBe(visible);
        actual.addButton.shouldBe(visible);
        return this;
    }

    public AdminProductsPageAssert productsTableIsVisible() {
        actual.productsTable.shouldBe(visible);
        return this;
    }

    public AdminProductsPageAssert nameFieldContains(String value) {
        actual.nameField.shouldHave(com.codeborne.selenide.Condition.value(value));
        return this;
    }

    public AdminProductsPageAssert priceFieldContains(String value) {
        actual.priceField.shouldHave(com.codeborne.selenide.Condition.value(value));
        return this;
    }

    public AdminProductsPageAssert notificationContains(String message) {
        actual.toastContainer.shouldHave(text(message));
        return this;
    }

    public AdminProductsPageAssert productsTableContains(String value) {
        actual.productsTable.shouldHave(text(value));
        return this;
    }
}
