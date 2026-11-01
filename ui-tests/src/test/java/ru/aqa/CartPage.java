package ru.aqa;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/** Page object for the cart modal. */
public class CartPage extends BasePage {
    final SelenideElement modal = $("#cartModal");
    final SelenideElement items = $("#cart-items");
    final ElementsCollection cartItems = $$("#cart-items .cart-item");
    final SelenideElement totalPrice = $("#total-price");
    final SelenideElement placeOrderButton = $("#makeOrder");
    final SelenideElement emptyMessage = $("#empty-cart");

    public CartPageAssert check() {
        return new CartPageAssert(this);
    }

    @Step("Remove product '{productName}' from cart")
    public CartPage removeProduct(String productName) {
        cartItem(productName).$("[data-action='remove']").click();
        return this;
    }

    @Step("Place order from cart")
    public CartPage placeOrder() {
        placeOrderButton.click();
        return this;
    }

    @Step("Accept order confirmation alert")
    public CartPage acceptOrderAlert() {
        WebDriverRunner.getWebDriver().switchTo().alert().accept();
        return this;
    }

    @Step("Read order confirmation alert text")
    String orderAlertText() {
        return WebDriverRunner.getWebDriver().switchTo().alert().getText();
    }

    @Step("Find cart item '{productName}'")
    SelenideElement cartItem(String productName) {
        return cartItems.findBy(text(productName));
    }
}
