package ru.aqa;

import org.assertj.core.api.AbstractAssert;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static org.assertj.core.api.Assertions.assertThat;

/** Assertions for storefront elements. */
public class MainPageAssert extends BasePageAssert<MainPageAssert, MainPage> {
    public MainPageAssert(MainPage actual) {
        super(actual, MainPageAssert.class);
    }

    @Step("Check that the storefront title is visible")
    public MainPageAssert titleIsVisible() {
        actual.title.shouldBe(visible);
        return this;
    }

    @Step("Check that the product list is visible")
    public MainPageAssert productsListIsVisible() {
        actual.productsList.shouldBe(visible);
        return this;
    }

    @Step("Check that the storefront has at least one product")
    public MainPageAssert hasAtLeastOneProduct() {
        assertThat(actual.productCards.size()).isGreaterThan(0);
        return this;
    }

    @Step("Check that product '{productName}' is visible")
    public MainPageAssert productIsVisible(String productName) {
        actual.productCard(productName).shouldBe(visible);
        return this;
    }

    @Step("Check that product '{productName}' contains text '{expectedText}'")
    public MainPageAssert productContainsText(String productName, String expectedText) {
        actual.productCard(productName).shouldBe(visible)
                .shouldHave(com.codeborne.selenide.Condition.text(expectedText));
        return this;
    }

    @Step("Check that the admin link is visible")
    public MainPageAssert adminLinkIsVisible() {
        actual.adminLink.shouldBe(visible);
        return this;
    }

    @Step("Check that the cart button is visible")
    public MainPageAssert cartButtonIsVisible() {
        actual.cartButton.shouldBe(visible);
        return this;
    }

    @Step("Check that the cart count is '{count}'")
    public MainPageAssert cartCountContains(String count) {
        actual.cartCount.shouldBe(visible)
                .shouldHave(com.codeborne.selenide.Condition.exactText(count));
        return this;
    }

    @Step("Check that notification contains '{message}'")
    public MainPageAssert notificationContains(String message) {
        actual.toastContainer.shouldHave(com.codeborne.selenide.Condition.text(message));
        return this;
    }
}
