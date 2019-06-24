package com.mycompany;

public class Employee {

    private final String name;
    private final String surname;

    private final transient String title;

   // private final static long projects = 15L;

    Employee(String name, String surname, String title) {
        this.name = name;
        this.surname = surname;
        this.title = title;
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
