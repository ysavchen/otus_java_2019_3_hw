package com.mycompany;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Employee {

    private final String name;
    private final String surname;

    private transient String title = "software engineer";

    private final boolean isActive = true;

    private final Set<Integer> integerSet;

    private final List<String> responsibilities = List.of("Coding", "Reviewing");

    private final HashMap<Integer, String> integerStringHashMap;

    private final static long projects = 15L;

    Employee(String name, String surname) {
        this.name = name;
        this.surname = surname;
        this.integerSet = new HashSet<>();
        integerSet.add(56);
        integerSet.add(78);

        this.integerStringHashMap = new HashMap<>();
        integerStringHashMap.put(36, "Thirty six");
        integerStringHashMap.put(89, "Eighty nine");
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
