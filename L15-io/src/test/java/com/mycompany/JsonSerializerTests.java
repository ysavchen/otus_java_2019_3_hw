package com.mycompany;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    void customCheck() {
        Gson gson = new Gson();

        assertEquals(gson.toJson(null), JsonSerializer.toJson(null));
        assertEquals(gson.toJson((byte) 1), JsonSerializer.toJson((byte) 1));
        assertEquals(gson.toJson((short) 1f), JsonSerializer.toJson((short) 1f));
        assertEquals(gson.toJson(1), JsonSerializer.toJson(1));
        assertEquals(gson.toJson(1L), JsonSerializer.toJson(1L));
        assertEquals(gson.toJson(1f), JsonSerializer.toJson(1f));
        assertEquals(gson.toJson(1d), JsonSerializer.toJson(1d));
        assertEquals(gson.toJson("aaa"), JsonSerializer.toJson("aaa"));
        assertEquals(gson.toJson('a'), JsonSerializer.toJson('a'));
        assertEquals(gson.toJson(new int[]{1, 2, 3}), JsonSerializer.toJson(new int[]{1, 2, 3}));
        assertEquals(gson.toJson(List.of(1, 2, 3)), JsonSerializer.toJson(List.of(1, 2, 3)));
        //assertEquals(gson.toJson(Collections.singletonList(1)), JsonSerializer.toJson(Collections.singletonList(1)));
    }
}
