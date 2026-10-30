package ru.aqa;

import org.assertj.core.api.AbstractAssert;

import static com.codeborne.selenide.Condition.visible;
import static org.assertj.core.api.Assertions.assertThat;

/** Assertions for storefront elements. */
public class MainPageAssert extends AbstractAssert<MainPageAssert, MainPage> {
    public MainPageAssert(MainPage actual) {
        super(actual, MainPageAssert.class);
    }

    public MainPageAssert titleIsVisible() {
        actual.title.shouldBe(visible);
        return this;
    }

    public MainPageAssert productsListIsVisible() {
        actual.productsList.shouldBe(visible);
        return this;
    }

    public MainPageAssert hasAtLeastOneProduct() {
        assertThat(actual.productCards.size()).isGreaterThan(0);
        return this;
    }

    public MainPageAssert productIsVisible(String productName) {
        actual.productCard(productName).shouldBe(visible);
        return this;
    }

    public MainPageAssert productContainsText(String productName, String expectedText) {
        actual.productCard(productName).shouldBe(visible)
                .shouldHave(com.codeborne.selenide.Condition.text(expectedText));
        return this;
    }

    public MainPageAssert adminLinkIsVisible() {
        actual.adminLink.shouldBe(visible);
        return this;
    }

    public MainPageAssert cartButtonIsVisible() {
        actual.cartButton.shouldBe(visible);
        return this;
    }

    public MainPageAssert cartCountContains(String count) {
        actual.cartCount.shouldBe(visible)
                .shouldHave(com.codeborne.selenide.Condition.exactText(count));
        return this;
    }

    public MainPageAssert notificationContains(String message) {
        actual.toastContainer.shouldHave(com.codeborne.selenide.Condition.text(message));
        return this;
    }
}
