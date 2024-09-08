package com.urise.webapp;

import java.util.Arrays;
import java.util.List;

public class MainStream {
    public static void main(String[] args) {
        System.out.println(minValue(new int[]{1, 2, 3, 3, 2, 3}));
        System.out.println(minValue(new int[]{9,8}));
        System.out.println(oddOrEven(List.of(new Integer[]{1, 2, 3, 3, 2, 3, 5})));
    }

    private static int minValue(int[] values) {
        return Arrays.stream(values).distinct().sorted().reduce((acc, x) -> acc * 10 + x).getAsInt();
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        Integer sum = integers.stream().reduce(0, Integer::sum);
        return integers.stream().filter(a -> sum % 2 > 0 ? a % 2 < 1 : a % 2 > 0).toList();
    }
}
