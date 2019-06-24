package com.mycompany;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

class JsonSerializerTests {

    @Test
    void checkEmployee() {
        String empString = JsonSerializer.toJson(new Employee("William", "Johnson"));
        System.out.println("Serialized employee: " + empString);
        Gson gson = new Gson();
        gson.fromJson(empString, Employee.class);
    }

    @Test
    void checkDepartment() {
        String depString = JsonSerializer.toJson(new Department());
        System.out.println("Serialized department: " + depString);
        Gson gson = new Gson();
        gson.fromJson(depString, Department.class);
    }
}
