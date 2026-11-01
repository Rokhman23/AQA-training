package ru.aqa;

import org.assertj.core.api.AbstractAssert;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

/** Assertions for admin product management. */
public class AdminProductsPageAssert extends AbstractAssert<AdminProductsPageAssert, AdminProductsPage> {
    public AdminProductsPageAssert(AdminProductsPage actual) {
        super(actual, AdminProductsPageAssert.class);
    }

    @Step("Check that product creation form is visible")
    public AdminProductsPageAssert productFormIsVisible() {
        actual.nameField.shouldBe(visible);
        actual.priceField.shouldBe(visible);
        actual.addButton.shouldBe(visible);
        return this;
    }

    @Step("Check that products table is visible")
    public AdminProductsPageAssert productsTableIsVisible() {
        actual.productsTable.shouldBe(visible);
        return this;
    }

    @Step("Check that name field contains the expected value")
    public AdminProductsPageAssert nameFieldContains(String value) {
        actual.nameField.shouldHave(com.codeborne.selenide.Condition.value(value));
        return this;
    }

    @Step("Check that price field contains the expected value")
    public AdminProductsPageAssert priceFieldContains(String value) {
        actual.priceField.shouldHave(com.codeborne.selenide.Condition.value(value));
        return this;
    }

    @Step("Check that notification contains '{message}'")
    public AdminProductsPageAssert notificationContains(String message) {
        actual.toastContainer.shouldHave(text(message));
        return this;
    }

    @Step("Check that products table contains '{value}'")
    public AdminProductsPageAssert productsTableContains(String value) {
        actual.productsTable.shouldHave(text(value));
        return this;
    }
}
