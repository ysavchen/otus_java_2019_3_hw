package com.mycompany.dao;

import com.mycompany.annotations.Column;
import com.mycompany.annotations.Id;
import com.mycompany.annotations.Table;

@Table(name = "Account")
public class Account {

    @Id
    private final long no;

    @Column(name = "type")
    private final String type;

    @Column(name = "rest")
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
