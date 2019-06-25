package com.mycompany.dao;

import com.mycompany.annotations.Id;

public class Account {

    @Id
    private final long no;

    private final String type;

    private final int rest;

    public Account(long no, String type, int rest) {
        this.no = no;
        this.type = type;
        this.rest = rest;
    }

    public long getNo() {
        return no;
    }

    public String getType() {
        return type;
    }

    public int getRest() {
        return rest;
    }

    @Override
    public String toString() {
        return "Account{" +
                "no=" + no +
                ", type='" + type + '\'' +
                ", rest=" + rest +
                '}';
    }
}
