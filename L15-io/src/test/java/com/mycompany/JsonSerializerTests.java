package com.mycompany;

import org.junit.jupiter.api.Test;

public class JsonSerializerTests {

    @Test
    void checkArrayOfPrimitives() {

    }

    @Test
    void checkArrayOfObjects() throws IllegalAccessException {
        Department[] departments = {new Department(), new Department()};
        JsonSerializer.toJson(departments);
    }

    @Test
    void checkLists() {

    }

    @Test
    void checkSets() {

    }

    @Test
    void checkHashMaps() {

    }
}
