package ru.aqa;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("selenide")
@DisplayName("Drag-and-drop тесты корзины")
class GoodsDragAndDropTests {
    @BeforeEach
    void configureBrowser() {
        Configuration.browser = TestConfig.value("browser.name");
        Configuration.timeout = TestConfig.elementsTimeoutMs();
        AllureSelenideListener.register();
        TestConfig.printSafeSettings();
    }

    @AfterEach
    void closeBrowser() {
        Selenide.closeWebDriver();
    }

    @Test
    void dragProductToCartAndCheckItIsDisplayed() {
        String productName = TestConfig.starterProductName();
        MainPage mainPage = new MainPage().open();
        mainPage.check().productIsVisible(productName);

        mainPage.dragProductToCart(productName)
                .check().containsProduct(productName);
    }

    @Test
    void removeProductFromCartAndCheckItIsNoLongerDisplayed() {
        String productName = TestConfig.starterProductName();
        MainPage mainPage = new MainPage().open();
        CartPage cartPage = mainPage.dragProductToCart(productName);
        cartPage.check().containsProduct(productName);

        cartPage.removeProduct(productName);
        cartPage.check().doesNotContainProduct(productName).isEmpty();
    }
}
