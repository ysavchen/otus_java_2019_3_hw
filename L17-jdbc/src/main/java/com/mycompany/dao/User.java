package com.mycompany.dao;

import com.mycompany.annotations.Column;
import com.mycompany.annotations.Id;
import com.mycompany.annotations.Table;

@Table(name = "User")
public class User {

    @Id
    private final long id;

    @Column(name = "name")
    private final String name;

    @Column(name = "age")
    private int age;

    public User(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
