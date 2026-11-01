package ru.aqa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("hw2")
class BasicJavaTasksTest {
    private static final Random RANDOM = new Random();
    private static final List<String> NAMES = List.of("Dima", "Vitya", "Tigran");

    @BeforeEach
    void beforeEach() {
        System.out.println("========================Test method start");
    }

    @AfterEach
    void afterEach() {
        System.out.println("Test method end\n========================");
    }

    private void assertTaskResult(int taskNumber, Object input, Object actual, Object expected) {
        String details = "Задача " + taskNumber
                + " | Ввод: " + format(input)
                + " | ожидалось: " + format(expected)
                + " | получено: " + format(actual);
        if (expected instanceof Object[] expectedArray && actual instanceof Object[] actualArray) {
            assertArrayEquals(expectedArray, actualArray, details);
        } else if (expected instanceof Double expectedDouble && actual instanceof Double actualDouble) {
            assertEquals(expectedDouble, actualDouble, 0.000000001, details);
        } else {
            assertEquals(expected, actual, details);
        }
        System.out.println("Задача " + taskNumber
                + " | Ввод: " + format(input)
                + " | Результат: " + format(actual)
                + " | TEST PASSED");
    }

    private void assertNewStructure(int taskNumber, String check, Object original, Object result) {
        String details = "Задача " + taskNumber + " | ожидалось: новый объект для проверки «"
                + check + "», получено: " + (original == result ? "тот же объект" : "новый объект");
        assertNotSame(original, result, details);
        System.out.println("Задача " + taskNumber + " | " + check + ": да | TEST PASSED");
    }

    private String format(Object value) {
        if (value instanceof Object[] array) return Arrays.deepToString(array);
        return String.valueOf(value);
    }

    // Задача 1
    @Order(1)
    @RepeatedTest(10)
    void isEvenWorksForRandomNumber() {
        int value = RANDOM.nextInt(1, 101);
        boolean expected = value % 2 == 0;
        boolean actual = BasicJavaTasks.isEven(value);
        assertTaskResult(1, value, actual, expected);
    }

    // Задача 2
    @Order(2)
    @RepeatedTest(20)
    void checkAccessWorksForRandomAge() {
        int age = RANDOM.nextInt(0, 100);
        String expected = age > 18 ? "Allowed" : "Denied";
        String actual = BasicJavaTasks.checkAccess(age);
        assertTaskResult(2, age, actual, expected);
    }

    // Задача 3
    @Order(3)
    @RepeatedTest(10)
    void isPositiveWorksForRandomNumber() {
        int value = RANDOM.nextInt(-1000, 1001);
        boolean actual = BasicJavaTasks.isPositive(value);
        boolean expected = value >= 0;
        assertTaskResult(3, value, actual, expected);
    }

    // Задача 4
    @Order(4)
    @ParameterizedTest
    @MethodSource("randomScores")
    void getGradeWorksForRandomScore(int score) {
        String expected = score < 0 || score > 100 ? "Error"
                : score <= 20 ? "E" : score <= 40 ? "D" : score <= 60 ? "C" : score <= 80 ? "B" : "A";
        String actual = BasicJavaTasks.getGrade(score);
        assertTaskResult(4, score, actual, expected);
    }

    static IntStream randomScores() {
        return IntStream.concat(RANDOM.ints(20, 0, 101), IntStream.of(-1, 0, 20, 21, 40, 41, 60, 61, 80, 81, 100, 101));
    }

    // Задача 5
    @Order(5)
    @RepeatedTest(10)
    void blastOffWorksForRandomStart() {
        int start = RANDOM.nextInt(1, 11);
        String expected = IntStream.iterate(start, n -> n >= 1, n -> n - 1)
                .mapToObj(Integer::toString).reduce((a, b) -> a + " " + b).orElse("") + " Поехали!";
        String actual = BasicJavaTasks.blastOff(start);
        assertTaskResult(5, start, actual, expected);
    }

    // Задача 6
    @Order(6)
    @ParameterizedTest
    @MethodSource("randomPositiveNumbers")
    void sumToNWorksForRandomNumber(int n) {
        int expected = n * (n + 1) / 2;
        int actual = BasicJavaTasks.sumToN(n);
        assertTaskResult(6, n, actual, expected);
    }

    static IntStream randomPositiveNumbers() {
        return RANDOM.ints(10, 1, 101);
    }

    // Задача 7
    @Order(7)
    @RepeatedTest(10)
    void hasBugWorksForRandomMessages() {
        String[] values = {"Ready", "OK", RANDOM.nextBoolean() ? "Bug" : "Clean"};
        boolean expected = Arrays.stream(values).anyMatch(v -> v.equalsIgnoreCase("Bug"));
        boolean actual = BasicJavaTasks.hasBug(values);
        assertTaskResult(7, Arrays.toString(values), actual, expected);
    }

    // Задача 8
    @Order(8)
    @RepeatedTest(10)
    void getEvenInRangeWorksForRandomRange() {
        int start = RANDOM.nextInt(0, 50);
        int end = RANDOM.nextInt(0, 50);
        String expected = IntStream.rangeClosed(Math.min(start, end), Math.max(start, end)).filter(n -> n % 2 == 0)
                .mapToObj(Integer::toString).reduce((a, b) -> a + " " + b).orElse("");
        String actual = BasicJavaTasks.getEvenInRange(start, end);
        assertTaskResult(8, start + ".." + end, actual, expected);
        assertEquals("2 4", BasicJavaTasks.getEvenInRange(5, 2),
                "Задача 8 | Ввод: 5..2 | ожидалось: 2 4 | получено: " + BasicJavaTasks.getEvenInRange(5, 2));
    }

    // Задача 9
    @Order(9)
    @ParameterizedTest
    @MethodSource("randomArrays")
    void findMaxWorksForRandomArray(int[] values) {
        int expected = Arrays.stream(values).max().orElseThrow();
        int actual = BasicJavaTasks.findMax(values);
        assertTaskResult(9, Arrays.toString(values), actual, expected);
    }

    static Stream<int[]> randomArrays() {
        return Stream.generate(() -> RANDOM.ints(10, -100, 101).toArray()).limit(4);
    }

    // Задача 10
    @Order(10)
    @RepeatedTest(10)
    void reverseWorksForRandomArray() {
        String[] values = {"One", "Two", "Zero", Integer.toString(RANDOM.nextInt(100))};
        String[] expected = IntStream.range(0, values.length).mapToObj(i -> values[values.length - 1 - i]).toArray(String[]::new);
        String[] actual = BasicJavaTasks.reverse(values);
        assertTaskResult(10, Arrays.toString(values), actual, expected);
        assertNewStructure(10, "Новый массив", values, actual);
    }

    // Задача 11
    @Order(11)
    @RepeatedTest(10)
    void calcAverageWorksForRandomNumbers() {
        List<Integer> values = RANDOM.ints(5, 0, 101).boxed().toList();
        double expected = values.stream().mapToInt(Integer::intValue).average().orElseThrow();
        double actual = BasicJavaTasks.calcAverage(values);
        assertTaskResult(11, values, actual, expected);
    }

    // Задача 12
    @Order(12)
    @ParameterizedTest
    @MethodSource("randomNameLists")
    void removeSpecificNameWorksForRandomList(List<String> names) {
        String remove = "Vitya";
        List<String> expected = names.stream().filter(name -> !name.equals(remove)).toList();
        List<String> actual = BasicJavaTasks.removeSpecificName(names, remove);
        assertTaskResult(12, names + ", remove=" + remove, actual, expected);
        assertNewStructure(12, "Новый список", names, actual);
    }

    static Stream<List<String>> randomNameLists() {
        return Stream.generate(() -> {
            List<String> names = new ArrayList<>(NAMES);
            Collections.shuffle(names, RANDOM);
            return names;
        }).limit(10);
    }
}
