package com.mycompany.dao;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Account")
public class Account {

    @Id
    @GeneratedValue
    private Long no;

    @Column(name = "type")
    private String type;

    @Column(name = "rest")
    private Integer rest;

    public Account() {
    }

    public Account setNo(long no) {
        this.no = no;
        return this;
    }

    public Account setType(String type) {
        this.type = type;
        return this;
    }

    public Account setRest(int rest) {
        this.rest = rest;
        return this;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return no.equals(account.no) &&
                rest.equals(account.rest) &&
                Objects.equals(type, account.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(no, type, rest);
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
