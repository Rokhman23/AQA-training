package ru.aqa;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/** Page object for the storefront and its product list. */
public class MainPage {
    private static final String URL = TestConfig.uiUrl();

    final SelenideElement title = $("#main-title");
    final SelenideElement productsList = $("#products-list");
    final ElementsCollection productCards = $$(".product-card");
    final SelenideElement adminLink = $("a[href='/admin']");
    final SelenideElement cartButton = $("#open-cart-btn");
    final SelenideElement cartCount = $("#cart-count");
    final SelenideElement toastContainer = $("#toast-container");

    public MainPageAssert check() {
        return new MainPageAssert(this);
    }

    @Step("Open storefront")
    public MainPage open() {
        Selenide.open(URL);
        return this;
    }

    @Step("Refresh storefront page")
    public MainPage refresh() {
        Selenide.refresh();
        return this;
    }

    @Step("Open admin login page from storefront")
    public AdminLoginPage openAdmin() {
        adminLink.click();
        return new AdminLoginPage();
    }

    @Step("Open cart")
    public CartPage openCart() {
        cartButton.click();
        return new CartPage();
    }

    @Step("Set quantity {quantity} for product '{productName}'")
    public MainPage setProductQuantity(String productName, int quantity) {
        productCard(productName).$("input").setValue(String.valueOf(quantity));
        return this;
    }

    @Step("Add product '{productName}' to cart")
    public MainPage addProductToCart(String productName) {
        productCard(productName).$("[data-action='add-to-cart']").click();
        return this;
    }

    @Step("Drag product '{productName}' to the cart")
    public CartPage dragProductToCart(String productName) {
        productCard(productName).dragAndDrop(
                com.codeborne.selenide.DragAndDropOptions.to("#open-cart-btn"));
        return openCart();
    }

    @Step("Find product card '{productName}'")
    SelenideElement productCard(String productName) {
        return productCards.findBy(text(productName));
    }
}
