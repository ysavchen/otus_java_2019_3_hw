package com.mycompany.executor;

import com.mycompany.ReflectionUtils;
import com.mycompany.annotations.Id;
import com.mycompany.exceptions.NoIdFoundException;
import com.mycompany.exceptions.SeveralIdsFoundException;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JdbcTemplateImpl implements JdbcTemplate {

    private final Connection connection;

    public JdbcTemplateImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Object object) throws SQLException {
        Field[] fields = object.getClass().getDeclaredFields();
        checkIdPresent(object, fields);

        DbExecutor<?> executor = new DbExecutorImpl<>(connection);
        String table = object.getClass().getSimpleName();
        String columns = Stream.of(fields)
                .map(Field::getName)
                .collect(Collectors.joining(", "));

        Consumer<PreparedStatement> paramsSetter = pst -> {
            //todo: idx incremented twice within one cycle
            for (int idx = 0; idx < fields.length; idx++) {
                try {
                    setParameter(pst, idx + 1, fields[idx].get(object));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        executor.insertRecord(
                "insert into " + table + "(" + columns + ")" +
                        " values (?, ?, ?)", paramsSetter);
        connection.commit();
    }


    @Override
    public void update(Object object) throws SQLException {
        Field[] fields = object.getClass().getDeclaredFields();
        checkIdPresent(object, fields);
        DbExecutor<?> executor = new DbExecutorImpl<>(connection);

        String table = object.getClass().getSimpleName();
        StringBuilder update = new StringBuilder();
        update.append("set ");
        for (int i = 0; i < fields.length; i++) {
            String name = fields[i].getName();
            if (i > 0) {
                update.append(", ");
            }
            update.append(name).append(" = ?");
        }

        Consumer<PreparedStatement> paramsSetter = pst -> {
            //todo: idx incremented twice within one cycle
            for (int idx = 0; idx < fields.length; idx++) {
                try {
                    setParameter(pst, idx + 1, fields[idx].get(object));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        executor.insertRecord("update " + table + " " + update, paramsSetter);
        connection.commit();
    }


    @Override
    public <T> T load(long id, Class<T> clazz) throws SQLException {
        Object object = ReflectionUtils.instantiate(clazz);
        Field[] fields = object.getClass().getDeclaredFields();
        checkIdPresent(object, fields);
        DbExecutor<T> executor = new DbExecutorImpl<>(connection);

        String table = object.getClass().getSimpleName();
        String columns = Stream.of(fields)
                .map(Field::getName)
                .collect(Collectors.joining(", "));

        String idField = Stream.of(fields)
                .filter(field -> {
                            field.setAccessible(true);
                            return field.getAnnotation(Id.class) != null;
                        }
                ).findFirst()
                .orElseThrow(() -> new NoIdFoundException("Entity does not have a field with @Id - " + object))
                .getName();

        T entity = executor.selectRecord(
                "select " + columns + " from " + table + " where " + idField + "  = ?", id,
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            int idx = 1;
                            for (Field field : fields) {
                                field.setAccessible(true);
                                try {
                                    Object value = getValue(resultSet, idx, field.getType());
                                    field.set(object, value);
                                    idx++;
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                            return clazz.cast(object);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    return null;
                });

        connection.commit();
        return entity;
    }

    private void checkIdPresent(Object object, Field[] fields) {
        int numIds = 0;
        for (var field : fields) {
            field.setAccessible(true);
            if (field.getAnnotation(Id.class) != null) {
                numIds++;
            }
        }

        if (numIds == 0) {
            throw new NoIdFoundException("Entity does not have a field with @Id - " + object);
        }
        if (numIds > 1) {
            throw new SeveralIdsFoundException("Entity have several fields with @Id - " + object);
        }
    }

    private void setParameter(PreparedStatement pst, int idx, Object object) throws SQLException {
        if (object == null) {
            return;
        }
        if (object.getClass() == String.class) {
            pst.setString(idx, String.valueOf(object));
        }
        if (object.getClass() == Integer.class || object.getClass() == int.class) {
            pst.setInt(idx, (Integer) object);
        }
        if (object.getClass() == Long.class || object.getClass() == long.class) {
            pst.setLong(idx, (Long) object);
        }
        if (object.getClass() == Double.class || object.getClass() == double.class) {
            pst.setDouble(idx, (Double) object);
        }
        if (object.getClass() == Float.class || object.getClass() == float.class) {
            pst.setFloat(idx, (Float) object);
        }
        if (object.getClass() == Byte.class || object.getClass() == byte.class) {
            pst.setByte(idx, (Byte) object);
        }
        if (object.getClass() == Short.class || object.getClass() == short.class) {
            pst.setShort(idx, (Short) object);
        }
        if (object.getClass() == Boolean.class || object.getClass() == boolean.class) {
            pst.setBoolean(idx, (Boolean) object);
        }
    }

    private Object getValue(ResultSet rs, int idx, Class<?> typeOfField) throws SQLException {
        if (typeOfField == String.class) {
            return rs.getString(idx);
        }
        if (typeOfField == Integer.class || typeOfField == int.class) {
            return rs.getInt(idx);
        }
        if (typeOfField == Long.class || typeOfField == long.class) {
            return rs.getLong(idx);
        }
        if (typeOfField == Double.class || typeOfField == double.class) {
            return rs.getDouble(idx);
        }
        if (typeOfField == Float.class || typeOfField == float.class) {
            return rs.getFloat(idx);
        }
        if (typeOfField == Byte.class || typeOfField == byte.class) {
            return rs.getByte(idx);
        }
        if (typeOfField == Short.class || typeOfField == short.class) {
            return rs.getShort(idx);
        }
        if (typeOfField == Boolean.class || typeOfField == boolean.class) {
            return rs.getBoolean(idx);
        }
        return null;
    }

}
