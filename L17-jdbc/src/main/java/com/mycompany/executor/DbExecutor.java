package com.mycompany.executor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public interface DbExecutor<T> {

    void insertRecord(String sql, Consumer<PreparedStatement> paramsSetter) throws SQLException;

    T selectRecord(String sql, long id, Function<ResultSet, T> rsHandler) throws SQLException;
}
