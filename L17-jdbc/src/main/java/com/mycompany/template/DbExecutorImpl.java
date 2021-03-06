package com.mycompany.template;

import java.sql.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class DbExecutorImpl<T> implements DbExecutor<T> {

    private final Connection connection;

    public DbExecutorImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insertRecord(String sql, Consumer<PreparedStatement> paramsSetter) throws SQLException {
        System.out.println("SQL for insert: " + sql);
        Savepoint savePoint = this.connection.setSavepoint("savePointName");
        PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        try (pst) {
            paramsSetter.accept(pst);
            pst.executeUpdate();
        } catch (SQLException ex) {
            this.connection.rollback(savePoint);
            throw ex;
        }
    }

    @Override
    public T selectRecord(String sql, long id, Function<ResultSet, T> rsHandler) throws SQLException {
        System.out.println("SQL for select: " + sql);
        try (PreparedStatement pst = this.connection.prepareStatement(sql)) {
            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return rsHandler.apply(rs);
            }
        }
    }
}
