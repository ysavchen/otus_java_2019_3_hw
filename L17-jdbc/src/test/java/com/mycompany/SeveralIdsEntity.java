package com.mycompany;

import com.mycompany.annotations.Id;

public class SeveralIdsEntity {

    @Id
    private long id;

    @Id
    private String name;

    private int age;

}
