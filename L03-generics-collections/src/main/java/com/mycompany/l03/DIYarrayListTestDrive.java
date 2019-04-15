package com.mycompany.l03;

import java.util.Collections;
import java.util.List;

/**
 * Test the following methods:
 * <p>
 * Collections.addAll(Collection<? super T> c, T... elements)
 * Collections.static <T> void copy(List<? super T> dest, List<? extends T> src)
 * Collections.static <T> void sort(List<T> list, Comparator<? super T> c)
 */
public class DIYarrayListTestDrive {

    public static void main(String... args) {

        List<Integer> intList = new DIYarrayList<>();

        //test addAll()
        if (Collections.addAll(intList, generateInts(2))) {
            //intList.forEach(System.out::println);
            System.out.println(intList.size());
            System.out.println("Collections.addAll() - passed");
        }

        //test copy()
        List<Integer> newList = new DIYarrayList<>();
        Collections.addAll(newList, generateInts(5));
        Collections.copy(newList, intList);
    }

    private static Integer[] generateInts(int numElements) {
        Integer[] intArray = new Integer[numElements];
        for (int i = 0; i < numElements; i++) {
            intArray[i] = i;
        }
        return intArray;
    }
}
