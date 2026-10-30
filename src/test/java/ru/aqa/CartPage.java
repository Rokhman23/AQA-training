package ru.aqa;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/** Page object for the cart modal. */
public class CartPage {
    final SelenideElement modal = $("#cartModal");
    final SelenideElement items = $("#cart-items");
    final ElementsCollection cartItems = $$("#cart-items .cart-item");
    final SelenideElement totalPrice = $("#total-price");
    final SelenideElement placeOrderButton = $("#makeOrder");
    final SelenideElement emptyMessage = $("#empty-cart");

    public CartPageAssert check() {
        return new CartPageAssert(this);
    }

    public CartPage removeProduct(String productName) {
        cartItem(productName).$("[data-action='remove']").click();
        return this;
    }

    public CartPage placeOrder() {
        placeOrderButton.click();
        return this;
    }

    public CartPage acceptOrderAlert() {
        WebDriverRunner.getWebDriver().switchTo().alert().accept();
        return this;
    }

    String orderAlertText() {
        return WebDriverRunner.getWebDriver().switchTo().alert().getText();
    }

    SelenideElement cartItem(String productName) {
        return cartItems.findBy(text(productName));
    }
}
