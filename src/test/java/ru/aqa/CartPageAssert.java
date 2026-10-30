package ru.aqa;

import org.assertj.core.api.AbstractAssert;

import static com.codeborne.selenide.Condition.*;
import static org.assertj.core.api.Assertions.assertThat;

/** Assertions for cart contents and order feedback. */
public class CartPageAssert extends AbstractAssert<CartPageAssert, CartPage> {
    public CartPageAssert(CartPage actual) {
        super(actual, CartPageAssert.class);
    }

    public CartPageAssert isVisible() {
        actual.modal.shouldBe(visible);
        return this;
    }

    public CartPageAssert itemListIsVisible() {
        actual.items.shouldBe(visible);
        return this;
    }

    public CartPageAssert placeOrderButtonIsVisible() {
        actual.placeOrderButton.shouldBe(visible);
        return this;
    }

    public CartPageAssert containsProduct(String name) {
        actual.cartItem(name).shouldBe(visible);
        return this;
    }

    public CartPageAssert doesNotContainProduct(String name) {
        assertThat(actual.cartItems.filterBy(text(name)).size()).isZero();
        return this;
    }

    public CartPageAssert totalEquals(int expected) {
        actual.totalPrice.shouldHave(exactText(String.valueOf(expected)));
        return this;
    }

    public CartPageAssert totalIsAtMost(int maximum) {
        int total = Integer.parseInt(actual.totalPrice.getText().trim());
        assertThat(total).isLessThanOrEqualTo(maximum);
        return this;
    }

    public CartPageAssert isEmpty() {
        actual.emptyMessage.shouldBe(visible).shouldHave(text("Пусто"));
        return this;
    }

    public CartPageAssert orderAlertContains(String expectedText) {
        assertThat(actual.orderAlertText()).contains(expectedText);
        return this;
    }
}
