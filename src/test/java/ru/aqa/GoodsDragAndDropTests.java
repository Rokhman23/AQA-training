package ru.aqa;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.DragAndDropOptions;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

@Tag("selenide")
class GoodsDragAndDropTests {
    private static final String BASE_URL = TestConfig.uiUrl();
    private static final String PRODUCT_NAME = TestConfig.starterProductName();

    @BeforeEach
    void configureBrowser() {
        Configuration.browser = "chrome";
        Configuration.timeout = TestConfig.elementsTimeoutMs();
        TestConfig.printSafeSettings();
    }

    @AfterEach
    void closeBrowser() {
        Selenide.closeWebDriver();
    }

    @Test
    void dragProductToCartAndCheckItIsDisplayed() {
        open(BASE_URL);

        $$(".product-card").findBy(text(PRODUCT_NAME))
                .dragAndDrop(DragAndDropOptions.to("#open-cart-btn"));
        $("#open-cart-btn").click();

        $("#cart-items .cart-item").shouldHave(text(PRODUCT_NAME));
    }

    @Test
    void removeProductFromCartAndCheckItIsNoLongerDisplayed() {
        open(BASE_URL);

        $$(".product-card").findBy(text(PRODUCT_NAME))
                .dragAndDrop(DragAndDropOptions.to("#open-cart-btn"));
        $("#open-cart-btn").click();
        $("#cart-items .cart-item").shouldHave(text(PRODUCT_NAME));

        $("#cart-items .cart-item [data-action='remove']").click();

        $("#cart-items .cart-item").shouldNot(exist);
        $("#empty-cart").shouldHave(text("Пусто"));
    }
}
