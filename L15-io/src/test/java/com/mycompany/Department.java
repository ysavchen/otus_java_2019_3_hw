package com.mycompany;

public class Department {

    private final String name = "Application Services";

    private final int numEmployees = 45;

    private final transient boolean isActive = true;


    private final Employee[] engineers = {
            new Employee("Marcus", "Schmidt", "software engineer"),
            new Employee("Luke", "Wyatt", "software engineer")
    };

    private final String[] typesOfServices = {"Internal", "External"};

    private final int[] ints = {1, 2, 3};
//    /*
//    private final List<Employee> managers = List.of(
//            new Employee("William", "Johnson", "manager"),
//            new Employee("Benjamin", "Brown", "manager"));
//
//     */

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
