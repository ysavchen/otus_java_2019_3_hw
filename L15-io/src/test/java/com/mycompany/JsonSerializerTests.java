package com.mycompany;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.util.List;

class JsonSerializerTests {

    @Test
    void checkArrayOfPrimitives() {
        String str = JsonSerializer.toJson(new Department());
        Gson gson = new Gson();
        Department dep = gson.fromJson(str, Department.class);
        System.out.println(dep);
    }

    @Test
    void checkArrayOfObjects() {
        Department[] departments = {new Department(), new Department()};
        String str = JsonSerializer.toJson(departments);
        System.out.println(str);
    }

    @Test
    void checkLists() {
        List<Employee> empList = List.of(
                new Employee("Marcus", "Wendler", "software engineer"),
                new Employee("Luke", "Wyatt", "software engineer"));
        JsonSerializer.toJson(empList);

//        String str = new Gson().toJson(empList);
//        System.out.println(str);

    }

    @Test
    void checkSets() {
        Gson gson = new Gson();
        String str = gson.toJson(new Employee("Marcus", "Wendler", "software engineer"));
        System.out.println(str);
        Employee emp = gson.fromJson(str, Employee.class);
    }

    @Test
    void checkHashMaps() {
        int[] ints = {1, 2, 3};
        JsonSerializer.toJson(ints);
    }
}
