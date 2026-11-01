package ru.aqa;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.opentest4j.AssertionFailedError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("hw3")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BasicJavaAssertionsTest {

    // Задача 1: проверка метода с булевым результатом.
    @Order(1)
    @RepeatedTest(10)
    void isPositiveReturnsInformativeBooleanAssertion() {
        int input = 7;
        boolean actual = BasicJavaTasks.isPositive(input);
        assertTrue(actual, "Задача 1 | Ввод: " + input + " | ожидалось: true | получено: " + actual);
    }

    // Задача 2: проверка метода, который возвращает список.
    @Order(2)
    @RepeatedTest(10)
    void removeSpecificNameReturnsExpectedList() {
        List<String> input = List.of("Dima", "Vitya", "Tigran");
        List<String> expected = List.of("Dima", "Tigran");
        List<String> actual = BasicJavaTasks.removeSpecificName(input, "Vitya");
        assertEquals(expected, actual,
                "Задача 2 | Удаляем Vitya из " + input + " | ожидалось: " + expected + " | получено: " + actual);
    }

    // Задача 3: обычная проверка строкового результата.
    @Order(3)
    @RepeatedTest(10)
    void blastOffReturnsExpectedCountdown() {
        int input = 3;
        String expected = "3 2 1 Поехали!";
        String actual = BasicJavaTasks.blastOff(input);
        assertEquals(expected, actual,
                "Задача 3 | Ввод: " + input + " | ожидалось: " + expected + " | получено: " + actual);
    }

    // Задача 4: намеренно неверное ожидание. assertThrows подтверждает,
    // что внутренний assertEquals падает и показывает expected/actual,
    // но весь учебный прогон при этом остаётся успешным.
    @Order(4)
    @RepeatedTest(10)
    void incorrectExpectedGradeProducesInformativeAssertionFailure() {
        int input = 90;
        String expectedByMethod = BasicJavaTasks.getGrade(input);
        AssertionFailedError failure = assertThrows(AssertionFailedError.class,
                () -> assertEquals("B", expectedByMethod,
                        "Задача 4 | Ввод: " + input + " | ожидалось: B | получено: " + expectedByMethod));

        String failureMessage = failure.getMessage();
        assertTrue(failureMessage.contains("ожидалось: B") && failureMessage.contains("получено: A"),
                "Задача 4 | Сообщение о падении должно содержать expected и actual; получено: " + failureMessage);
    }
}
