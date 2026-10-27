package ru.aqa;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("selenium")
class GoodsSeleniumTests {
    private static final String BASE_URL = TestConfig.uiUrl();
    private static final String API_URL = TestConfig.apiUrl();
    private static final String USER = TestConfig.adminUsername();
    private static final String PASSWORD = TestConfig.adminPassword();
    private WebDriver driver;
    private String productName;
    private Long productId;

    @BeforeEach
    void openBrowser() {
        TestConfig.printSafeSettings();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(TestConfig.elementsTimeoutMs()));
        
        productName = "Selenium " + UUID.randomUUID().toString().substring(0, 8);
    }

    @AfterEach
    void closeBrowserAndDeleteTestProduct() {
        if (driver != null) driver.quit();
        if (productId != null) {
            given().auth().preemptive().basic(USER, PASSWORD)
                    .delete(API_URL + "/goods/{id}", productId);
        }
    }

    @Test
    void addProductThroughAdminAndSeeItOnStorefront() {
        openAdmin();
        driver.findElement(By.id("n-name")).sendKeys(productName);
        driver.findElement(By.id("n-price")).sendKeys("70");
        driver.findElement(By.id("add-btn")).click();
        new WebDriverWait(driver, Duration.ofMillis(TestConfig.elementsTimeoutMs())).until(
                ExpectedConditions.visibilityOfElementLocated(By.id("toast-container")));
        productId = findProductIdByName(productName);
        driver.get(BASE_URL + "/");
        new WebDriverWait(driver, Duration.ofMillis(TestConfig.elementsTimeoutMs())).until(
                ExpectedConditions.textToBePresentInElementLocated(By.id("products-list"), productName));
        assertThat(driver.findElement(By.id("products-list")).getText()).contains(productName);
    }

    @Test
    void addProductToCartAndSeeItThere() {
        productName = TestConfig.starterProductName();
        driver.get(BASE_URL + "/");
        driver.findElement(By.cssSelector(".product-card:has(h4)"));
        var card = driver.findElements(By.cssSelector(".product-card")).stream()
                .filter(element -> element.getText().contains(productName)).findFirst().orElseThrow();
        card.findElement(By.cssSelector("[data-action='add-to-cart']")).click();
        driver.findElement(By.id("open-cart-btn")).click();
        new WebDriverWait(driver, Duration.ofMillis(TestConfig.elementsTimeoutMs())).until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#cart-items .cart-item")));
        assertThat(driver.findElement(By.id("cart-items")).getText()).contains(productName);
        assertThat(driver.findElement(By.id("total-price")).getText())
                .isEqualTo(String.valueOf(TestConfig.starterProductPrice()));
    }

    @Test
    void wrongCredentialsAreRejectedByAdmin() {
        driver.get(BASE_URL + "/admin");
        driver.findElement(By.id("username")).sendKeys("wrong-user");
        driver.findElement(By.id("password")).sendKeys("wrong-password");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        new WebDriverWait(driver, Duration.ofMillis(TestConfig.elementsTimeoutMs())).until(
                ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        assertThat(driver.findElement(By.id("username")).isDisplayed()).isTrue();
    }

    @Test
    void cartRemainsAfterPageRefresh() {
        productName = TestConfig.starterProductName();
        driver.get(BASE_URL + "/");
        var card = driver.findElements(By.cssSelector(".product-card")).stream()
                .filter(element -> element.getText().contains(productName)).findFirst().orElseThrow();
        card.findElement(By.cssSelector("[data-action='add-to-cart']")).click();
        driver.navigate().refresh();
        driver.findElement(By.id("open-cart-btn")).click();
        assertThat(driver.findElement(By.id("cart-items")).getText()).contains(productName);
        assertThat(driver.findElement(By.id("total-price")).getText())
                .isEqualTo(String.valueOf(TestConfig.starterProductPrice()));
    }

    private void openAdmin() {
        driver.get(BASE_URL + "/admin");
        driver.findElement(By.id("username")).sendKeys(USER);
        driver.findElement(By.id("password")).sendKeys(PASSWORD);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        new WebDriverWait(driver, Duration.ofMillis(TestConfig.elementsTimeoutMs())).until(
                ExpectedConditions.visibilityOfElementLocated(By.id("n-name")));
    }

    private Long findProductIdByName(String name) {
        return given().auth().preemptive().basic(USER, PASSWORD)
                .get(API_URL + "/goods/list").then().statusCode(200)
                .extract().jsonPath().getList("goods").stream()
                .filter(item -> name.equals(((java.util.Map<?, ?>) item).get("name")))
                .map(item -> ((Number) ((java.util.Map<?, ?>) item).get("id")).longValue())
                .findFirst().orElse(null);
    }
}




