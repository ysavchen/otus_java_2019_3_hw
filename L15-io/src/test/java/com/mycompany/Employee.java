package com.mycompany;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Employee {

    private final String name;
    private final String surname;

    private String someField = null;

    private transient String title = "software engineer";

    private final boolean isActive = true;

    private final Set<Integer> integerSet;

    private final List<String> responsibilities = List.of("Coding", "Reviewing");

    private final static long projects = 15L;

    Employee(String name, String surname) {
        this.name = name;
        this.surname = surname;
        this.integerSet = new HashSet<>();
        integerSet.add(56);
        integerSet.add(78);
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
