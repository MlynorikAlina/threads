package com.bsu;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

enum Parameters {
    SORT_BY_DIGITS_NUM_INCREASING(3),
    SORT_BY_DIGITS_NUM_DECREASING(4),
    SORT_BY_VALUE_DECREASING(2),
    SORT_BY_VALUE_INCREASING(1),
    DEFAULT(-1);

    private final int value;

    Parameters(int val) {
        this.value = val;
    }

    public static Parameters fromInt(int value) {
        for (Parameters type : values()) {
            if (type.value == value) return type;
        }
        return Parameters.DEFAULT;
    }

}

class ArrSort implements Runnable {
    List<Integer> array;
    Parameters order;

    public ArrSort(List<Integer> array, int order) {
        this.array = array;
        this.order = Parameters.fromInt(order);
    }

    @Override
    public void run() {
        switch (order) {
            case SORT_BY_DIGITS_NUM_INCREASING:
                array.sort(Comparator.comparingInt(x -> x.toString().length()));
                break;
            case SORT_BY_DIGITS_NUM_DECREASING:
                array.sort(Comparator.comparingInt(x -> x.toString().length()).reversed());
                break;
            case SORT_BY_VALUE_DECREASING:
                array.sort(Comparator.reverseOrder());
                break;
            case SORT_BY_VALUE_INCREASING:
                array.sort(Integer::compareTo);
                break;
            case DEFAULT:
                throw new InvalidParameterException("No such an option");
        }
    }
}

public class Main {
    final static int L_BOUND = 0;
    final static int U_BOUND = 2000;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter n: ");
            int arrayLength = scanner.nextInt();
            if (arrayLength < 0) throw new InvalidParameterException("Length should be natural");
            List<Integer> array = getArray(arrayLength);
            printArray(array);
            printMenu();
            int sortOrder = scanner.nextInt();

            Thread thread = new Thread(new ArrSort(array, sortOrder));
            thread.start();

            thread.join();
            printArray(array);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public static List<Integer> getArray(int n) {
        List<Integer> array = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            array.add((int) (Math.random() * (U_BOUND - L_BOUND)) + L_BOUND);
        }
        return array;
    }

    public static void printArray(List<Integer> array) {
        System.out.println("Array: ");
        array.forEach(el -> System.out.print(el + " "));
        System.out.print(System.lineSeparator());
    }

    public static void printMenu() {
        System.out.println("1. Sort by value (increasing)\n" +
                "2. Sort by value (decreasing)\n" +
                "3. Sort by the number of digits (increasing)\n" +
                "4. Sort by the number of digits (decreasing)\n");
    }
}
