package ru.aqa;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

final class TestConfig {
    private static final Properties PROPERTIES = loadProperties();

    private TestConfig() {
    }

    static String uiUrl() { return required("ui.url"); }
    static String apiUrl() { return required("api.url"); }
    static long elementsTimeoutMs() { return Long.parseLong(required("elements.timeout.ms")); }
    static String loggingMode() { return required("logging.mode"); }
    static String adminUsername() { return required("admin.username"); }
    static String adminPassword() { return required("admin.password"); }
    static String starterProductName() { return required("starter.product.name"); }
    static int starterProductPrice() { return Integer.parseInt(required("starter.product.price")); }

    static void printSafeSettings() {
        System.out.println("Test configuration:");
        System.out.println("  UI URL: " + uiUrl());
        System.out.println("  API URL: " + apiUrl());
        System.out.println("  Elements timeout (ms): " + elementsTimeoutMs());
        System.out.println("  Logging mode: " + loggingMode());
        System.out.println("  Starter product name: " + starterProductName());
        System.out.println("  Starter product price: " + starterProductPrice());
    }

    private static String required(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required test configuration property: " + key);
        }
        return value.trim();
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream stream = TestConfig.class.getResourceAsStream("/test-config.properties")) {
            if (stream == null) {
                throw new IllegalStateException("Could not find /test-config.properties on the test classpath");
            }
            properties.load(new InputStreamReader(stream, StandardCharsets.UTF_8));
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException("Could not read /test-config.properties", e);
        }
    }
}
