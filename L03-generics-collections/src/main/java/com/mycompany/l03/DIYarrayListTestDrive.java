package com.mycompany.l03;

import java.util.Collection;
import java.util.List;

public class DIYarrayListTestDrive {

    public static void main(String... args) {
        Collection<Integer> intsCollection = List.of(
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
                11, 12, 13, 14, 15, 16, 17, 18, 19, 20
        );

        Collection<Integer> integers = new DIYarrayList<>();
        integers.addAll(intsCollection);
    }
}
