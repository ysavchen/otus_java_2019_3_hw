package com.mycompany;

import com.google.gson.Gson;
import com.mycompany.no_visitor.JsonSerializer;
import org.junit.jupiter.api.Test;

class JsonSerializerTests {

    @Test
    void checkArrayOfPrimitives() throws IllegalAccessException {
        String str = JsonSerializer.toJson(new Department());
        Gson gson = new Gson();
        Department dep = gson.fromJson(str, Department.class);
        System.out.println(dep);
    }

    @Test
    void checkArrayOfObjects() throws IllegalAccessException {
        Department[] departments = {new Department(), new Department()};
        JsonSerializer.toJson(departments);
    }

    @Test
    void checkLists() {
        com.mycompany.with_visitor.JsonSerializer.toJson(new Department());
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
