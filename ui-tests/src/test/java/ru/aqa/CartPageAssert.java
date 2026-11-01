package ru.aqa;

import org.assertj.core.api.AbstractAssert;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static org.assertj.core.api.Assertions.assertThat;

/** Assertions for cart contents and order feedback. */
public class CartPageAssert extends BasePageAssert<CartPageAssert, CartPage> {
    public CartPageAssert(CartPage actual) {
        super(actual, CartPageAssert.class);
    }

    @Step("Check that cart modal is visible")
    public CartPageAssert isVisible() {
        actual.modal.shouldBe(visible);
        return this;
    }

    @Step("Check that cart item list is visible")
    public CartPageAssert itemListIsVisible() {
        actual.items.shouldBe(visible);
        return this;
    }

    @Step("Check that place-order button is visible")
    public CartPageAssert placeOrderButtonIsVisible() {
        actual.placeOrderButton.shouldBe(visible);
        return this;
    }

    @Step("Check that cart contains product '{name}'")
    public CartPageAssert containsProduct(String name) {
        actual.cartItem(name).shouldBe(visible);
        return this;
    }

    @Step("Check that cart does not contain product '{name}'")
    public CartPageAssert doesNotContainProduct(String name) {
        assertThat(actual.cartItems.filterBy(text(name)).size()).isZero();
        return this;
    }

    @Step("Check that cart total equals {expected}")
    public CartPageAssert totalEquals(int expected) {
        actual.totalPrice.shouldHave(exactText(String.valueOf(expected)));
        return this;
    }

    @Step("Check that cart total does not exceed {maximum}")
    public CartPageAssert totalIsAtMost(int maximum) {
        int total = Integer.parseInt(actual.totalPrice.getText().trim());
        assertThat(total).isLessThanOrEqualTo(maximum);
        return this;
    }

    @Step("Check that cart is empty")
    public CartPageAssert isEmpty() {
        actual.emptyMessage.shouldBe(visible).shouldHave(text(TestConfig.value("message.cart.empty")));
        return this;
    }

    @Step("Check that order alert contains '{expectedText}'")
    public CartPageAssert orderAlertContains(String expectedText) {
        assertThat(actual.orderAlertText()).contains(expectedText);
        return this;
    }
}
