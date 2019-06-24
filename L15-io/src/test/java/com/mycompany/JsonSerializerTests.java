package com.mycompany;

import com.google.gson.Gson;
import com.mycompany.no_visitor.JsonSerializer;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;

class JsonSerializerTests {

    @Test
    void checkArrayOfPrimitives() throws IllegalAccessException {
        String str = JsonSerializer.toJson(new Department());
        Gson gson = new Gson();
        Department dep = gson.fromJson(str, Department.class);
        System.out.println(dep);
    }

    @Test
    void checkArrayOfObjects() {
        Department[] departments = {new Department(), new Department()};
        String str = com.mycompany.with_visitor.JsonSerializer.toJson(departments);
        Gson gson = new Gson();
        Department[] dep = (Department[]) gson.fromJson(str, Object.class);
        System.out.println(dep);
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
