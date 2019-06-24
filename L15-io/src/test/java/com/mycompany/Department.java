package com.mycompany;

import java.util.Arrays;
import java.util.List;

public class Department {

    private final String name = "Application Services";

    private final int numEmployees = 45;

    private final transient boolean isActive = true;

    private final Employee[] developers = {
            new Employee("Henry", "Schmidt"),
            new Employee("Luke", "Wyatt")
    };

    private final String[] typesOfServices = {"Internal", "External"};

    private final int[] ints = {1, 2, 3};

    private final List<Employee> operations = List.of(
            new Employee("William", "Johnson"),
            new Employee("Benjamin", "Brown"));

    @Override
    public String toString() {
        return "Department{" +
                "name='" + name + '\'' +
                ", numEmployees=" + numEmployees +
                ", developers=" + Arrays.toString(developers) +
                ", operations=" + operations +
                '}';
    }
}
