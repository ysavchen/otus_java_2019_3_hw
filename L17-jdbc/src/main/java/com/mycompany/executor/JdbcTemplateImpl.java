package com.mycompany.executor;

import com.mycompany.ReflectionUtils;
import com.mycompany.annotations.Convert;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class JdbcTemplateImpl implements JdbcTemplate {

    private final Connection connection;

    private final Map<RequestTypes, RequestPattern> reqDetailsMap = new HashMap<>();

    public JdbcTemplateImpl(Connection connection) {

        this.connection = connection;
        for (var reqType : RequestTypes.values()) {
            reqDetailsMap.put(reqType, new RequestPattern(reqType));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void create(Object object) {
        Objects.requireNonNull(object);
        String sqlRequest = reqDetailsMap.get(RequestTypes.INSERT).analyze(object);

        Consumer<PreparedStatement> paramsSetter = pst -> {
            int idx = 1;
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                try {
                    Object value = field.get(object);

                    Convert convert = field.getAnnotation(Convert.class);
                    if (convert != null) {
                        value = ReflectionUtils.instantiate(convert.converter())
                                .convertToDatabaseColumn(value);
                    }
                    pst.setObject(idx, value);
                    idx++;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        DbExecutor<?> executor = new DbExecutorImpl<>(connection);
        System.out.println(sqlRequest);
        try {
            executor.insertRecord(sqlRequest, paramsSetter);
            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void update(Object object) {
        Objects.requireNonNull(object);

        String sqlRequest = reqDetailsMap.get(RequestTypes.UPDATE).analyze(object);

        Consumer<PreparedStatement> paramsSetter = pst -> {
            int idx = 1;
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                try {
                    Object value = field.get(object);

                    Convert convert = field.getAnnotation(Convert.class);
                    if (convert != null) {
                        value = ReflectionUtils.instantiate(convert.converter())
                                .convertToDatabaseColumn(value);
                    }
                    pst.setObject(idx, value);
                    idx++;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        DbExecutor<?> executor = new DbExecutorImpl<>(connection);
        System.out.println(sqlRequest);
        try {
            executor.insertRecord(sqlRequest, paramsSetter);
            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T load(long id, Class<T> clazz) {
        Object object = ReflectionUtils.instantiate(clazz);

        String sqlRequest = reqDetailsMap.get(RequestTypes.LOAD).analyze(object);
        DbExecutor<T> executor = new DbExecutorImpl<>(connection);

        System.out.println(sqlRequest);
        T entity = null;
        try {
            entity = executor.selectRecord(
                    sqlRequest, id, resultSet -> {
                        try {
                            if (resultSet.next()) {
                                int idx = 1;
                                for (Field field : clazz.getDeclaredFields()) {
                                    field.setAccessible(true);

                                    Object value = resultSet.getObject(idx);

                                    Convert convert = field.getAnnotation(Convert.class);
                                    if (convert != null) {
                                        value = ReflectionUtils.instantiate(convert.converter())
                                                .convertToEntityAttribute(value);
                                    }
                                    field.set(object, value);
                                    idx++;
                                }
                                return clazz.cast(object);
                            }
                        } catch (SQLException | IllegalAccessException ex) {
                            ex.printStackTrace();
                        }
                        return null;
                    });

            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return entity;
    }

}
