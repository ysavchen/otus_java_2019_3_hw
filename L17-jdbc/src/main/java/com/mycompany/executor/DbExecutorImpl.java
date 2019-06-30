package com.mycompany.executor;

import java.sql.*;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class DbExecutorImpl<T> implements DbExecutor<T> {

    private final Connection connection;

    public DbExecutorImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public long insertRecord(String sql, Consumer<PreparedStatement> consumer) throws SQLException {
        System.out.println(sql);
        Savepoint savePoint = this.connection.setSavepoint("savePointName");
        PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        try (pst) {
            consumer.accept(pst);
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            this.connection.rollback(savePoint);
            throw ex;
        }
    }

    @Override
    public Optional<T> selectRecord(String sql, long id, Function<ResultSet, T> rsHandler) throws SQLException {
        try (PreparedStatement pst = this.connection.prepareStatement(sql)) {
            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return Optional.ofNullable(rsHandler.apply(rs));
            }
        }
    }
}