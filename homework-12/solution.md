# Домашнее задание. Тема 12. Ознакомительная часть. Завершение

```java
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

public class Homework12 {

    // Задача 1: числа по модулю, от большего к меньшему.
    public static Comparator<Integer> byAbsoluteValueDescending() {
        return Comparator.comparingLong(
                number -> Math.abs((long) number)
        ).reversed();
    }

    // Задача 2: строки с "sudo" будут первыми.
    public static Comparator<String> sudoFirst() {
        return Comparator.comparing(value -> !value.contains("sudo"));
    }

    // Задача 3: считаем числа больше 5.
    public static long countGreaterThanFive(Collection<Integer> numbers) {
        return numbers.stream()
                .filter(Objects::nonNull)
                .filter(number -> number > 5)
                .count();
    }

    // Задача 4: оставляем строки из цифр и превращаем их в Integer.
    public static Integer[] toIntegerArray(String[] values) {
        return Arrays.stream(values)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(value -> value.matches("\\d+"))
                .map(Homework12::parseIntegerOrNull)
                .filter(Objects::nonNull)
                .toArray(Integer[]::new);
    }

    private static Integer parseIntegerOrNull(String value) {
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }
}
```
