package com.mycompany.dbservices;

public interface DbServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);

}