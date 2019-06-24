package com.mycompany;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

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
        JsonSerializer.toJson(new Department());
    }

    @Test
    void checkSets() {
        Gson gson = new Gson();
        String str = gson.toJson(new Employee("Marcus", "Wendler", "software engineer"));
        System.out.println(str);
    }

    @Test
    void checkHashMaps() {

    }
}
