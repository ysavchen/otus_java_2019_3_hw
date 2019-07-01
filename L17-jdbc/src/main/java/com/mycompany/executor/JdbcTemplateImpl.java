package com.mycompany.executor;

import com.mycompany.ReflectionUtils;
import com.mycompany.annotations.Id;
import com.mycompany.exceptions.NoIdFoundException;
import com.mycompany.exceptions.SeveralIdsFoundException;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JdbcTemplateImpl implements JdbcTemplate {

    private static final String URL = "jdbc:h2:mem:";

    private Connection connection;

    private Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL);
                connection.setAutoCommit(false);
                createTables(connection);
            } catch (SQLException ex) {
                System.out.println("Connection is not created: " + ex.toString());
                throw new ExceptionInInitializerError(ex);
            }
        }
        return connection;
    }

    private void createTables(Connection connection) throws SQLException {
        PreparedStatement psUser = connection.prepareStatement(
                "create table User(" +
                        "id bigint(20) NOT NULL auto_increment, " +
                        "name varchar(255), " +
                        "age int(3))");
        PreparedStatement psAccount = connection.prepareStatement(
                "create table Account(" +
                        "no bigint(20) NOT NULL auto_increment, " +
                        "type varchar(255), " +
                        "rest number)");

        try (psUser; psAccount) {
            psUser.executeUpdate();
            psAccount.executeUpdate();
        }
    }

    @Override
    public void create(Object object) throws SQLException {
        Field[] fields = object.getClass().getDeclaredFields();
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

        Connection connection = getConnection();
        DbExecutor<?> executor = new DbExecutorImpl<>(connection);
        String table = object.getClass().getSimpleName();
        String columns = Stream.of(fields)
                .map(Field::getName)
                .collect(Collectors.joining(", "));

        Consumer<PreparedStatement> paramsSetter = pst -> {
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

        //todo: should connection be closed here?
        //connection.close();
    }


    public void setParameter(PreparedStatement pst, int idx, Object object) throws SQLException {
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

    @Override
    public void update(Object object) throws SQLException {
        Field[] fields = object.getClass().getDeclaredFields();
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

        Connection connection = getConnection();
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
            for (int idx = 0; idx < fields.length; idx++) {
                try {
                    setParameter(pst, idx + 1, fields[idx].get(object));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };


        executor.insertRecord(
                "update " + table + " " + update, paramsSetter);
        connection.commit();
    }

    @Override
    public <T> T load(long id, Class<T> clazz) throws SQLException {
        Connection connection = getConnection();
        DbExecutor<T> executor = new DbExecutorImpl<>(connection);

        Object object = ReflectionUtils.instantiate(clazz);

        Field[] fields = object.getClass().getDeclaredFields();
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

        String table = object.getClass().getSimpleName();
        String columns = Stream.of(fields)
                .map(Field::getName)
                .collect(Collectors.joining(", "));


        T entity = executor.selectRecord("select " + columns + " from " + table + " where id  = ?", id, resultSet -> {
            try {
                if (resultSet.next()) {
                    int idx = 1;
                    for (int i = 0; i < fields.length; i++) {
                        fields[i].setAccessible(true);
                        try {
                            Object value = getValue(resultSet, idx, fields[i].getType());
                            fields[i].set(object, value);
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
        return entity;
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
