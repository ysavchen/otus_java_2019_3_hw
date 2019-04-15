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

        //addAll()
        Collections.addAll(intList, generateInts(0, 25));

        //copy()
        List<Integer> otherList = new DIYarrayList<>();
        Collections.addAll(otherList, generateInts(30, 30));
        Collections.copy(otherList, intList);

        //sort()
        Collections.sort(otherList, Integer::compareTo);
    }

    private static Integer[] generateInts(int firstInt, int numElements) {
        Integer[] intArray = new Integer[numElements];
        for (int i = 0; i < numElements; i++) {
            intArray[i] = firstInt;
            firstInt++;
        }
        return intArray;
    }
}
