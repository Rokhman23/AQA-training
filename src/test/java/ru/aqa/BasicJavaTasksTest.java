package ru.aqa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

    private void report(int taskNumber, Object input, Object actual, Object expected) {
        boolean passed = Objects.deepEquals(actual, expected);
        System.out.println("Задача " + taskNumber
                + " | Ввод: " + format(input)
                + " | Результат: " + format(actual)
                + " | " + (passed ? "TEST PASSED" : "TEST FAILED"));
    }

    private void reportStructure(int taskNumber, String check, boolean passed) {
        System.out.println("Задача " + taskNumber + " | " + check + ": "
                + (passed ? "да | TEST PASSED" : "нет | TEST FAILED"));
    }

    private String format(Object value) {
        if (value instanceof Object[] array) return Arrays.deepToString(array);
        return String.valueOf(value);
    }

    // Задача 1
    @Order(1)
    @Test
    void isEvenWorksForRandomNumber() {
        int value = RANDOM.nextInt(1, 101);
        boolean expected = value % 2 == 0;
        boolean actual = BasicJavaTasks.isEven(value);
        report(1, value, actual, expected);
    }

    // Задача 2
    @Order(2)
    @RepeatedTest(20)
    void checkAccessWorksForRandomAge() {
        int age = RANDOM.nextInt(0, 100);
        String expected = age > 18 ? "Allowed" : "Denied";
        String actual = BasicJavaTasks.checkAccess(age);
        report(2, age, actual, expected);
    }

    // Задача 3
    @Order(3)
    @Test
    void isPositiveWorksForRandomNumber() {
        int value = RANDOM.nextInt(-1000, 1001);
        boolean actual = BasicJavaTasks.isPositive(value);
        boolean expected = value >= 0;
        report(3, value, actual, expected);
    }

    // Задача 4
    @Order(4)
    @ParameterizedTest
    @MethodSource("randomScores")
    void getGradeWorksForRandomScore(int score) {
        String expected = score <= 20 ? "E" : score <= 40 ? "D" : score <= 60 ? "C" : score <= 80 ? "B" : "A";
        String actual = BasicJavaTasks.getGrade(score);
        report(4, score, actual, expected);
        int[] boundaryScores = {-1, 0, 20, 21, 40, 41, 60, 61, 80, 81, 100, 101};
        String[] boundaryGrades = {"Error", "E", "E", "D", "D", "C", "C", "B", "B", "A", "A", "Error"};
        boolean boundariesPassed = true;
        for (int i = 0; i < boundaryScores.length; i++) {
            boundariesPassed &= boundaryGrades[i].equals(BasicJavaTasks.getGrade(boundaryScores[i]));
        }
        reportStructure(4, "Проверка границ диапазонов", boundariesPassed);
    }

    static IntStream randomScores() {
        return RANDOM.ints(20, 0, 101);
    }

    // Задача 5
    @Order(5)
    @RepeatedTest(4)
    void blastOffWorksForRandomStart() {
        int start = RANDOM.nextInt(1, 11);
        String expected = IntStream.iterate(start, n -> n >= 1, n -> n - 1)
                .mapToObj(Integer::toString).reduce((a, b) -> a + " " + b).orElse("") + " Поехали!";
        String actual = BasicJavaTasks.blastOff(start);
        report(5, start, actual, expected);
    }

    // Задача 6
    @Order(6)
    @ParameterizedTest
    @MethodSource("randomPositiveNumbers")
    void sumToNWorksForRandomNumber(int n) {
        int expected = n * (n + 1) / 2;
        int actual = BasicJavaTasks.sumToN(n);
        report(6, n, actual, expected);
    }

    static IntStream randomPositiveNumbers() {
        return RANDOM.ints(4, 1, 101);
    }

    // Задача 7
    @Order(7)
    @Test
    void hasBugWorksForRandomMessages() {
        String[] values = {"Ready", "OK", RANDOM.nextBoolean() ? "Bug" : "Clean"};
        boolean expected = Arrays.stream(values).anyMatch(v -> v.equalsIgnoreCase("Bug"));
        boolean actual = BasicJavaTasks.hasBug(values);
        report(7, Arrays.toString(values), actual, expected);
    }

    // Задача 8
    @Order(8)
    @RepeatedTest(4)
    void getEvenInRangeWorksForRandomRange() {
        int start = RANDOM.nextInt(0, 50);
        int end = RANDOM.nextInt(0, 50);
        String expected = IntStream.rangeClosed(Math.min(start, end), Math.max(start, end)).filter(n -> n % 2 == 0)
                .mapToObj(Integer::toString).reduce((a, b) -> a + " " + b).orElse("");
        String actual = BasicJavaTasks.getEvenInRange(start, end);
        report(8, start + ".." + end, actual, expected);
        reportStructure(8, "Перевёрнутые границы 5..2", "2 4".equals(BasicJavaTasks.getEvenInRange(5, 2)));
    }

    // Задача 9
    @Order(9)
    @ParameterizedTest
    @MethodSource("randomArrays")
    void findMaxWorksForRandomArray(int[] values) {
        int expected = Arrays.stream(values).max().orElseThrow();
        int actual = BasicJavaTasks.findMax(values);
        report(9, Arrays.toString(values), actual, expected);
    }

    static Stream<int[]> randomArrays() {
        return Stream.generate(() -> RANDOM.ints(10, -100, 101).toArray()).limit(4);
    }

    // Задача 10
    @Order(10)
    @Test
    void reverseWorksForRandomArray() {
        String[] values = {"One", "Two", "Zero", Integer.toString(RANDOM.nextInt(100))};
        String[] expected = IntStream.range(0, values.length).mapToObj(i -> values[values.length - 1 - i]).toArray(String[]::new);
        String[] actual = BasicJavaTasks.reverse(values);
        report(10, Arrays.toString(values), actual, expected);
        reportStructure(10, "Новый массив", actual != values);
    }

    // Задача 11
    @Order(11)
    @RepeatedTest(4)
    void calcAverageWorksForRandomNumbers() {
        List<Integer> values = RANDOM.ints(5, 0, 101).boxed().toList();
        double expected = values.stream().mapToInt(Integer::intValue).average().orElseThrow();
        double actual = BasicJavaTasks.calcAverage(values);
        report(11, values, actual, expected);
    }

    // Задача 12
    @Order(12)
    @ParameterizedTest
    @MethodSource("randomNameLists")
    void removeSpecificNameWorksForRandomList(List<String> names) {
        String remove = "Vitya";
        List<String> expected = names.stream().filter(name -> !name.equals(remove)).toList();
        List<String> actual = BasicJavaTasks.removeSpecificName(names, remove);
        report(12, names + ", remove=" + remove, actual, expected);
        reportStructure(12, "Новый список", actual != names);
    }

    static Stream<List<String>> randomNameLists() {
        return Stream.generate(() -> {
            List<String> names = new ArrayList<>(NAMES);
            Collections.shuffle(names, RANDOM);
            return names;
        }).limit(4);
    }
}
