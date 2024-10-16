package com.urise.webapp;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainStream {
    public static void main(String[] args) {
        System.out.println(minValue(new int[]{1, 2, 3, 3, 2, 3}));
        System.out.println(minValue(new int[]{9,8}));
        System.out.println(oddOrEven(List.of(new Integer[]{1, 2, 3, 3, 1, 3, 5})));
    }

    private static int minValue(int[] values) {
        return Arrays.stream(values).distinct().sorted().reduce(0, (acc, x) -> acc * 10 + x);
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        Map<Integer, List<Integer>> mapResult = integers.stream()
                .collect(Collectors.groupingBy(x -> x % 2, Collectors.toList()));
        return mapResult.get(1).size() % 2 == 0 ? mapResult.get(1) : mapResult.get(0);
    }
}