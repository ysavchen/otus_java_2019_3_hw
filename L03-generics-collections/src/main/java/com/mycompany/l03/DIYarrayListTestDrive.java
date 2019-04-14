package com.mycompany.l03;

import java.util.Collection;
import java.util.Collections;

public class DIYarrayListTestDrive {

    public static void main(String... args) {

        Collection<Integer> intList = new DIYarrayList<>();

        if (Collections.addAll(intList,
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
                11, 12, 13, 14, 15, 16, 17, 18, 19, 20)) {
            //intList.forEach(System.out::println);
            System.out.println("Collections.addAll() - passed");
        }
    }
}
