package ru.aqa;

import java.util.ArrayList;
import java.util.List;

public final class BasicJavaTasks {
    private BasicJavaTasks() {
    }

    // Задача 1
    public static boolean isEven(int n) {
        return n % 2 == 0;
    }

    // Задача 2
    public static String checkAccess(int age) {
        return age > 18 ? "Allowed" : "Denied";
    }

    // Задача 3
    public static boolean isPositive(int n) {
        return n >= 0 ? true : false;
    }

    // Задача 4
    public static String getGrade(int score) {
        if (score < 0 || score > 100) return "Error";
        if (score <= 20) return "E";
        if (score <= 40) return "D";
        if (score <= 60) return "C";
        if (score <= 80) return "B";
        return "A";
    }

    // Задача 5
    public static String blastOff(int start) {
        StringBuilder result = new StringBuilder();
        for (int value = start; value >= 1; value--) {
            if (!result.isEmpty()) result.append(' ');
            result.append(value);
        }
        if (!result.isEmpty()) result.append(' ');
        return result.append("Поехали!").toString();
    }

    // Задача 6
    public static int sumToN(int n) {
        int sum = 0;
        for (int value = 1; value <= n; value++) sum += value;
        return sum;
    }

    // Задача 7
    public static boolean hasBug(String[] messages) {
        if (messages == null) return false;
        for (String message : messages) {
            if ("Bug".equalsIgnoreCase(message)) return true;
        }
        return false;
    }

    // Задача 8
    public static String getEvenInRange(int start, int end) {
        StringBuilder result = new StringBuilder();
        int lowerBound = Math.min(start, end);
        int upperBound = Math.max(start, end);
        for (int value = lowerBound; value <= upperBound; value++) {
            if (value % 2 == 0) {
                if (!result.isEmpty()) result.append(' ');
                result.append(value);
            }
            if (value == Integer.MAX_VALUE) break;
        }
        return result.toString();
    }

    // Задача 9
    public static int findMax(int[] arr) {
        if (arr == null || arr.length == 0) throw new IllegalArgumentException("Array must not be empty");
        int max = arr[0];
        for (int value : arr) if (value > max) max = value;
        return max;
    }

    // Задача 10
    public static String[] reverse(String[] arr) {
        String[] reversed = new String[arr.length];
        for (int i = 0; i < arr.length; i++) reversed[i] = arr[arr.length - 1 - i];
        return reversed;
    }

    // Задача 11
    public static double calcAverage(List<? extends Number> numbers) {
        if (numbers == null || numbers.isEmpty()) throw new IllegalArgumentException("List must not be empty");
        double sum = 0;
        for (Number number : numbers) sum += number.doubleValue();
        return sum / numbers.size();
    }

    // Задача 12
    public static List<String> removeSpecificName(List<String> names, String nameToRemove) {
        List<String> result = new ArrayList<>(names);
        result.removeIf(name -> java.util.Objects.equals(name, nameToRemove));
        return result;
    }
}
