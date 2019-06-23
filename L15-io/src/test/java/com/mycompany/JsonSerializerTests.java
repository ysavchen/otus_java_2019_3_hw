package com.mycompany;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

class JsonSerializerTests {

    @Test
    void checkArrayOfPrimitives() throws IllegalAccessException {
        JsonSerializer.toJson(new Department());

//        Gson gson  = new Gson();
//        gson.toJson(ints);
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
