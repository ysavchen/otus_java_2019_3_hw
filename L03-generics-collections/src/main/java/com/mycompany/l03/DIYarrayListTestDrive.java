package com.mycompany.l03;

import org.apache.commons.lang3.RandomStringUtils;

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
        List<String> strList = new DIYarrayList<>();

        //addAll()
        Collections.addAll(strList, generateStrings(25, 5));
        System.out.println("strList after addAll(): ");
        strList.forEach(System.out::println);
        System.out.println();

        //copy()
        List<String> otherList = new DIYarrayList<>();
        Collections.addAll(otherList, generateStrings(30, 10));
        System.out.println("otherList before copy(): ");
        otherList.forEach(System.out::println);
        System.out.println();

        Collections.copy(otherList, strList);
        System.out.println("otherList after copy(): ");
        otherList.forEach(System.out::println);
        System.out.println();

        //sort()
        Collections.sort(otherList, String.CASE_INSENSITIVE_ORDER);
        System.out.println("otherList after sort(): ");
        otherList.forEach(System.out::println);
    }

    private static String[] generateStrings(int numElements, int length) {
        String[] array = new String[numElements];
        for (int i = 0; i < numElements; i++) {
            array[i] = RandomStringUtils.randomAlphabetic(length);
        }
        return array;
    }
}
