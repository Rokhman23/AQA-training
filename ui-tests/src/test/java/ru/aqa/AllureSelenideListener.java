package ru.aqa;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;

/** Installs the Allure listener once for each test JVM. */
public final class AllureSelenideListener {
    private static boolean registered;

    private AllureSelenideListener() {
    }

    public static synchronized void register() {
        if (!registered) {
            SelenideLogger.addListener("allure", new AllureSelenide()
                    .screenshots(true)
                    .savePageSource(true)
                    .includeSelenideSteps(true));
            registered = true;
        }
    }
}
