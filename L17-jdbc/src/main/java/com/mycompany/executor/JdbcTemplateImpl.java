package com.mycompany.executor;

import com.mycompany.ReflectionUtils;
import com.mycompany.annotations.Id;
import com.mycompany.exceptions.NoIdFoundException;
import com.mycompany.exceptions.SeveralIdsFoundException;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JdbcTemplateImpl implements JdbcTemplate {

    private final Connection connection;

    public JdbcTemplateImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Object object) {
        Objects.requireNonNull(object);

        Field[] fields = object.getClass().getDeclaredFields();
        checkIdPresent(object, fields);

        DbExecutor<?> executor = new DbExecutorImpl<>(connection);
        String table = object.getClass().getSimpleName();
        String columns = Stream.of(fields)
                .map(Field::getName)
                .collect(Collectors.joining(", "));

        Consumer<PreparedStatement> paramsSetter = pst -> {
            int idx = 1;
            for (Field field : fields) {
                try {
                    setParameter(pst, idx, field.get(object));
                    idx++;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        try {
            executor.insertRecord(
                    "insert into " + table + "(" + columns + ")" +
                            " values (?, ?, ?)", paramsSetter);
            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void update(Object object) {
        Objects.requireNonNull(object);

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
            int idx = 1;
            for (Field field : fields) {
                try {
                    setParameter(pst, idx, field.get(object));
                    idx++;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        try {
            executor.insertRecord("update " + table + " " + update, paramsSetter);
            connection.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public <T> T load(long id, Class<T> clazz) {
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

        T entity = null;
        try {
            entity = executor.selectRecord(
                    "select " + columns + " from " + table + " where " + idField + "  = ?", id,
                    resultSet -> {
                        try {
                            if (resultSet.next()) {
                                int idx = 1;
                                for (Field field : fields) {
                                    field.setAccessible(true);

                                    Object value = getValue(resultSet, idx, field.getType());
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

    /**
     * Checks if an object has an exactly one field marked by @Id.
     *
     * @param object object to insert/update/select
     * @param fields fields of the object
     */
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
            pst.setNull(idx, Types.NULL);
            return;
        }

        Class<?> objClass = object.getClass();
        if (objClass == String.class || objClass == Character.class || objClass == char.class) {
            pst.setString(idx, String.valueOf(object));
        }
        if (objClass == Integer.class || objClass == int.class) {
            pst.setInt(idx, (Integer) object);
        }
        if (objClass == Long.class || objClass == long.class) {
            pst.setLong(idx, (Long) object);
        }
        if (objClass == Double.class || objClass == double.class) {
            pst.setDouble(idx, (Double) object);
        }
        if (objClass == Float.class || objClass == float.class) {
            pst.setFloat(idx, (Float) object);
        }
        if (objClass == Byte.class || objClass == byte.class) {
            pst.setByte(idx, (Byte) object);
        }
        if (objClass == Short.class || objClass == short.class) {
            pst.setShort(idx, (Short) object);
        }
        if (objClass == Boolean.class || objClass == boolean.class) {
            pst.setBoolean(idx, (Boolean) object);
        }
    }

    private Object getValue(ResultSet rs, int idx, Class<?> typeOfField) throws SQLException {
        if (typeOfField == String.class || typeOfField == Character.class || typeOfField == char.class) {
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
