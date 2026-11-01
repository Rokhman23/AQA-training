package ru.aqa;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Properties;

/** Shared configuration for API and UI test modules. System properties and environment variables override the file. */
public final class TestConfig {
    private static final Properties PROPERTIES = loadProperties();

    private TestConfig() {
    }

    public static String value(String key) {
        String systemValue = System.getProperty(key);
        String environmentValue = System.getenv(toEnvironmentKey(key));
        String value = systemValue != null ? systemValue
                : environmentValue != null ? environmentValue
                : PROPERTIES.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required test configuration property: " + key);
        }
        return value.trim();
    }

    public static int integer(String key) {
        return Integer.parseInt(value(key));
    }

    public static long longValue(String key) {
        return Long.parseLong(value(key));
    }

    public static double decimal(String key) {
        return Double.parseDouble(value(key));
    }

    public static String uiUrl() { return value("ui.url"); }
    public static String apiUrl() { return value("api.url"); }
    public static long elementsTimeoutMs() { return longValue("elements.timeout.ms"); }
    public static String adminUsername() { return value("admin.username"); }
    public static String adminPassword() { return value("admin.password"); }
    public static String starterProductName() { return value("starter.product.name"); }
    public static int starterProductPrice() { return integer("starter.product.price"); }

    public static void printSafeSettings() {
        System.out.println("Test configuration:");
        System.out.println("  UI URL: " + uiUrl());
        System.out.println("  API URL: " + apiUrl());
        System.out.println("  Elements timeout (ms): " + elementsTimeoutMs());
        System.out.println("  Logging mode: " + value("logging.mode"));
        System.out.println("  Starter product name: " + starterProductName());
        System.out.println("  Starter product price: " + starterProductPrice());
    }

    private static String toEnvironmentKey(String key) {
        return key.replace('.', '_').replace('-', '_').toUpperCase(Locale.ROOT);
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream stream = TestConfig.class.getResourceAsStream("/test-config.properties")) {
            if (stream == null) {
                throw new IllegalStateException("Could not find /test-config.properties in the common module");
            }
            properties.load(new InputStreamReader(stream, StandardCharsets.UTF_8));
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException("Could not read common test configuration", e);
        }
    }
}
