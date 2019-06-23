package com.mycompany;

import java.util.Arrays;
import java.util.List;

public class Department {

    private final String name = "Application Services";

    private final int numEmployees = 45;


    private final Employee[] engineers = {
            new Employee("Marcus", "Schmidt", "software engineer"),
            new Employee("Luke", "Wyatt", "software engineer")
    };

    /*
    private final List<Employee> managers = List.of(
            new Employee("William", "Johnson", "manager"),
            new Employee("Benjamin", "Brown", "manager"));

     */

    @Override
    public String toString() {
        return "Department{" +
                "name='" + name + '\'' +
                ", numEmployees=" + numEmployees +
               // ", engineers=" + Arrays.toString(engineers) +
              //  ", managers=" + managers +
                '}';
    }
}
